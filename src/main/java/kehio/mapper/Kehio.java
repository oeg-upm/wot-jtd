package kehio.mapper;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.vocabulary.RDF;

public class Kehio {

	protected static final Resource KEHIO_TYPE = ResourceFactory.createResource("http://kehio.com/model#Thing");
	private static final List<RdfMapper> mappers = new ArrayList<>();
	static {
		
		// Order of inclusion in the list matters
		mappers.add(new RdfIdMapper());
		mappers.add(new RdfDatatypeContainerSerialiser());
		mappers.add(new RdfDatatypeCollectionSerialiser());
		mappers.add(new RdfDatatypeMapper());
		
		mappers.add(new RdfObjectContainerSerialiser());
		mappers.add(new RdfObjectCollectionSerialiser());
		mappers.add(new RdfObjectSerialiser());
		
		
	}
	
	
	
	
	public static Object serializeClass(Class<?> clazz, Model model, Resource subject) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		Object object = null;
		Field[] fields = new Field[] {};
		
		object = createInstance(clazz);
		Class<?> clazzFull = Class.forName(clazz.getName());
		// Retrieve fields from super-classes
		fields = extractFields(clazzFull);
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
	
	private static void instantiateObject(Object object, Field[] fields, Model model, Resource subject) {
		//model.write(System.out,"TTL");
		if(object!=null) {
			Set<String> processedProperties = new HashSet<>();
			for (int index=0; index < fields.length; index++) {
				Field field = fields[index];
			
				String processedProperty = processSerialiseFieldAnnotation(field, object, model, subject);
				
				if(processedProperty==null || processedProperty.isEmpty())
					continue;
				if(processedProperty!=null && !processedProperty.isEmpty())
					processedProperties.add(processedProperty);
				
			}
		}
	}
	
	private static String processSerialiseFieldAnnotation(Field field, Object object, Model model, Resource subject) {
		String annotationApplied = "";
		try {
			for (int index=0; index < mappers.size(); index++) {
			    RdfMapper serialiser = mappers.get(index);
				if(serialiser.hasProcesableAnnotation(field)) {
					annotationApplied = serialiser.fromRdfToObject(field, object, model, subject);
			    		break;
			    }
			}
		}catch(Exception e) {
			System.out.println(field);
			System.out.println(e.toString());
		}
		return annotationApplied;
	}
	
	private static Object createInstance(Class<?> clazz) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		return clazz.getConstructor().newInstance();
	}

   

	// -- Deserialisation methods
	
	
	public static Model deserializeClass(Object object) throws IllegalArgumentException, IllegalAccessException, URISyntaxException, ClassNotFoundException {
		Field[] fields = new Field[] {};
		Model model = ModelFactory.createDefaultModel();
	
		Class<?> clazzFull = object.getClass();
		// Retrieve fields from super-classes
		fields = extractFields(clazzFull);
		instantiateModel(object, fields, model);
		
		return model;
    }
	
	
	
	
	private static void instantiateModel(Object object, Field[] fields, Model model) throws IllegalArgumentException, IllegalAccessException, URISyntaxException {
		
		if(object!=null) {
			Resource subject = findSubjectResource(fields,object, model);
			model.remove(subject, RDF.type, Kehio.KEHIO_TYPE);

			for (int index=0; index < fields.length; index++) {
				Field field = fields[index];
				processDeserialiseFieldAnnotation(field, object, model, subject);
			}
		}
	}
	
	private static void processDeserialiseFieldAnnotation(Field field, Object object, Model model, Resource subject) throws IllegalArgumentException, IllegalAccessException, URISyntaxException {
			
		for (int index=0; index < mappers.size(); index++) {
			    RdfMapper mapper = mappers.get(index);
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
