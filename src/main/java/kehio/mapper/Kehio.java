package kehio.mapper;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.RDF;

public class Kehio {

	protected static final Resource KEHIO_TYPE = ResourceFactory.createResource("http://kehio.com/model#Thing");
	private static final List<RdfMapper> mappers = new ArrayList<>();
	static {
		
		// Order of inclusion in the list matters
		mappers.add(new RdfIdMapper());
		mappers.add(new RdfDatatypeGroupMapper());
		mappers.add(new RdfDatatypeMapper());
		
		mappers.add(new RdfObjectGroupMapper());
		mappers.add(new RdfObjectMapper());
		
	}
	

	public static Object serializeClass(Class<?> clazz, Model model, Resource subject) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, URISyntaxException {
		Object object = null;
		
		object = createInstance(clazz);
		Class<?> clazzFull = Class.forName(clazz.getName());
		// Retrieve fields from super-classes
		Field[] fields = extractFields(clazzFull);
		instantiateObject(object, fields, model, subject);
		
		return object;
    }
	
	private static Field[] extractFields(Class<? extends Object> clazz) throws ClassNotFoundException {
		// Extract fields
		Field[] fields = clazz.getDeclaredFields();
		// Extract super class fields
		AnnotatedType superAnnotatedClazz = clazz.getAnnotatedSuperclass();
		if(superAnnotatedClazz!=null) {
			Class<?> clazzFull = Class.forName(superAnnotatedClazz.getType().getTypeName());
			if(!clazzFull.equals(Object.class)) {
				Field[] superFields = extractFields(clazzFull);
				// Join current fields and super fields
				fields = ArrayUtils.addAll(fields, superFields);
			}
		}
		return fields;
	}
	
	private static void instantiateObject(Object object, Field[] fields, Model model, Resource subject) throws IllegalArgumentException, IllegalAccessException, URISyntaxException {
		if(object!=null) {
			Set<Property> processedProperties = new HashSet<>();
			for (int index=0; index < fields.length; index++) {
				Field field = fields[index];
				Set<Property> correctlyProcessedProperty = processSerialiseFieldAnnotation(field, object, model, subject);
				if(correctlyProcessedProperty!=null)
					processedProperties.addAll(correctlyProcessedProperty);
			}
			// add unknown RDF properties
			enhanceObjectWithUnknownProperties(fields, processedProperties, object, model, subject);
			// add unknown RDF triples
			enhanceObjectWithUnknownTriples(fields, processedProperties, object, model, subject);
		}
	}
	
	private static void enhanceObjectWithUnknownProperties(Field[] fields, Set<Property> processedProperties, Object object, Model model, Resource subject) throws IllegalArgumentException, IllegalAccessException, URISyntaxException {
		RdfPropertiesContainerMapper mapperContainer = new RdfPropertiesContainerMapper();
		mapperContainer.setPropertiesNotToContain(processedProperties);
		int unknownAnnotations = 0;
		for (int index=0; index < fields.length; index++) {
			Field field = fields[index];
			if(mapperContainer.hasProcesableAnnotation(field)) {
				if(unknownAnnotations == 0)
					mapperContainer.fromRdfToObject(field, object, model, subject);
				unknownAnnotations++;
			}else if(unknownAnnotations > 1) {
				throw new IllegalArgumentException("A Java class can be annotated only with one Container notation");
			}
		}
	}
	
	private static void enhanceObjectWithUnknownTriples(Field[] fields, Set<Property> processedProperties, Object object, Model model, Resource subject) throws IllegalArgumentException, IllegalAccessException, URISyntaxException {
		RdfContainerMapper mapperContainer = new RdfContainerMapper();
		mapperContainer.setPropertiesNotToContain(processedProperties);
		int unknownAnnotations = 0;
		for (int index=0; index < fields.length; index++) {
			Field field = fields[index];
			if(mapperContainer.hasProcesableAnnotation(field)) {
				if(unknownAnnotations == 0)
					mapperContainer.fromRdfToObject(field, object, model, subject);
				unknownAnnotations++;
			}else if(unknownAnnotations > 1) {
				throw new IllegalArgumentException("A Java class can be annotated only with one Container notation");
			}
		}
	}
	
	private static Set<Property> processSerialiseFieldAnnotation(Field field, Object object, Model model, Resource subject) {
		Set<Property> properties = null;
		try {
			for (int index=0; index < mappers.size(); index++) {
			    RdfMapper serialiser = mappers.get(index);
				if(serialiser.hasProcesableAnnotation(field)) {
					Property property = serialiser.fromRdfToObject(field, object, model, subject);
					
					properties = new HashSet<>();
					if(property!=null)
						properties.add(property);
			    		break;
			    }
			}
		}catch(Exception e) {
			throw new IllegalArgumentException(e.toString());
		}
		return properties;
	}
	
	private static Object createInstance(Class<?> clazz) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		return clazz.getConstructor().newInstance();
	}

   

	// -- Deserialisation methods
	
	
	public static Model deserializeClass(Object object) throws IllegalArgumentException, IllegalAccessException, URISyntaxException, ClassNotFoundException {
		return deserializeClassExtended(object).getValue();
   }
	
	public static Model deserializeClass(Object object,  Map<String,String> prefixes) throws IllegalArgumentException, IllegalAccessException, URISyntaxException, ClassNotFoundException {
		return deserializeClassExtended(object, prefixes).getValue();
    }
	
	protected static Entry<Resource, Model> deserializeClassExtended(Object object) throws IllegalArgumentException, IllegalAccessException, URISyntaxException, ClassNotFoundException {
		return deserializeClassExtended(object, null);
	}
	
	protected static Entry<Resource, Model> deserializeClassExtended(Object object, Map<String,String> prefixes) throws IllegalArgumentException, IllegalAccessException, URISyntaxException, ClassNotFoundException {
		Model model = ModelFactory.createDefaultModel();
		if(prefixes!=null && !prefixes.isEmpty())
			model.setNsPrefixes(prefixes);
		
		Class<?> clazzFull = object.getClass();
		// Retrieve fields from super-classes
		Field[] fields = extractFields(clazzFull);
		Resource subject = instantiateModel(object, fields, model);
		Entry<Resource, Model> entry = Map.entry(subject, model);
		return  entry;
    }
	
	
	
	private static Resource instantiateModel(Object object, Field[] fields, Model model) throws IllegalArgumentException, IllegalAccessException, URISyntaxException {
		Resource subject = null;
		if(object!=null) {
			subject = findSubjectResource(fields,object, model);
			model.remove(subject, RDF.type, Kehio.KEHIO_TYPE);

			for (int index=0; index < fields.length; index++) {
				Field field = fields[index];
				field.setAccessible(true);
				Object instantiatedField = field.get(object);
				if(instantiatedField!=null)
					processDeserialiseFieldAnnotation(field, object, model, subject);
			}
		}
		return subject;
	}
	
	private static void processDeserialiseFieldAnnotation(Field field, Object object, Model model, Resource subject) throws IllegalArgumentException, IllegalAccessException, URISyntaxException {
		List<RdfMapper> mappersAux = new ArrayList<>(mappers);
		mappersAux.add(new RdfPropertiesContainerMapper());
		mappersAux.add(new RdfContainerMapper());
		for (int index=0; index < mappersAux.size(); index++) {
			    RdfMapper mapper = mappersAux.get(index);
			     if(!(mapper instanceof RdfIdMapper) && mapper.hasProcesableAnnotation(field)) {
			    	 	mapper.fromObjectToRdf(field, object, model, subject);
			    		break;
					
			    }
			}
	}

	private static Resource findSubjectResource(Field[] fields, Object object, Model model) throws IllegalArgumentException, IllegalAccessException, URISyntaxException {
		RdfIdMapper idMapper = new RdfIdMapper();
		Resource subject = model.createResource();
		int rdfIds = 0;
		for (int index=0; index < fields.length; index++) {
			Field field = fields[index];
			if(idMapper.hasProcesableAnnotation(field)) {
				rdfIds++; 
				if(rdfIds==1) {
					idMapper.fromObjectToRdf(field, object, model, null);
				}else {
					throw new IllegalArgumentException(KehioUtils.concatStrings("@RdfId can be used only once in a class,. Please review annotation for attribute ", field.getName()));
				}			  
			}
		}
		// We remove the mapper
		if(model.isEmpty())
			model.add(subject, RDF.type, KEHIO_TYPE);
		return model.listSubjectsWithProperty(RDF.type, KEHIO_TYPE).next();
	}
	
	
	
}
