package wot.jtd;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

import com.apicatalog.jsonld.JsonLd;
import com.apicatalog.jsonld.JsonLdError;
import com.apicatalog.jsonld.document.Document;
import com.apicatalog.jsonld.document.JsonDocument;
import com.apicatalog.rdf.RdfDataset;
import com.apicatalog.rdf.RdfLiteral;
import com.apicatalog.rdf.RdfNQuad;
import com.apicatalog.rdf.RdfResource;
import com.apicatalog.rdf.RdfValue;
import com.google.gson.JsonObject;

public class RDFHandler {

	// -- to RDF methods
	
		public  Model toRDF(JsonObject td) throws JsonLdError {
			Model model = ModelFactory.createDefaultModel();
			InputStream tdStream = new ByteArrayInputStream(td.toString().getBytes());
			Document documentTD = JsonDocument.of(tdStream);
			RdfDataset dataset = JsonLd.toRdf(documentTD).get();
			Map<String,Resource> existingBlankNodes = new HashMap<>();
			dataset.toList().forEach(triple -> populateModel(triple, model, existingBlankNodes));
			return model;
		}
		
		private  void populateModel(RdfNQuad triple, Model model, Map<String,Resource> existingBlankNodes) {
			Resource subject = instantiateResource(triple.getSubject(), model, existingBlankNodes);
			Property predicate = ResourceFactory.createProperty(triple.getPredicate().getValue());

			RdfValue object = triple.getObject();
			if(object.isIRI()) {
				Resource objectJena = ResourceFactory.createResource(object.getValue());
				model.add(subject, predicate, objectJena);
			}else if(object.isBlankNode()){
				Resource objectJena = instantiateBlankNode(object.getValue(), model, existingBlankNodes);
				model.add(subject, predicate, objectJena);
			}else if(object.isLiteral()) {
				RdfLiteral literal = object.asLiteral();
				Optional<String> lang = literal.getLanguage();
				String datatype = literal.getDatatype();
				Literal jenaLiteral = ResourceFactory.createPlainLiteral(literal.getValue());
				if(lang.isPresent()) {
					jenaLiteral = ResourceFactory.createLangLiteral(literal.getValue(), lang.get());
				}else if(datatype!=null) {
					RDFDatatype dt = NodeFactory.getType(datatype);
					jenaLiteral = ResourceFactory.createTypedLiteral(literal.getValue(), dt);
				}
				model.add(subject, predicate, jenaLiteral);
			}
		}
		
		private  Resource instantiateResource(RdfResource resource, Model model, Map<String,Resource> existingBlankNodes) {
			Resource instantiatedResource = null;
			if(resource.isIRI()) {
				instantiatedResource = ResourceFactory.createResource(resource.getValue());
			}else {
				instantiatedResource = instantiateBlankNode(resource.getValue(), model, existingBlankNodes);
			}
			return instantiatedResource;
		}
		
		private  Resource instantiateBlankNode(String blankNode, Model model, Map<String,Resource> existingBlankNodes) {
			Resource instantiatedResource = null;
			if(!existingBlankNodes.containsKey(blankNode)) {
				instantiatedResource =  model.createResource();
				existingBlankNodes.put(blankNode, instantiatedResource);
			}else {
				instantiatedResource = existingBlankNodes.get(blankNode);
			}
			return instantiatedResource;
		}
}
