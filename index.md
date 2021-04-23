## Java API for Thing Descriptions of the Web of Things

The JDT is an open (Apache 2.0) ORM implementation of the current [Thing Description model standardised by the W3C Web of Things group](https://www.w3.org/TR/wot-thing-description/). The current features are:

* Serialise:
  * Serialise any Thing Description as a Java Object, i.e., a JDT
  * Serialise a JDT from a JSON-LD framed document
  * Serialise a JDT from a set of RDF triples
* Round trip-translation:
  *  Translate from a JSON-LD framed document into a set of equivalent RDF triples
  *  Translate a set of RDF triples into its equivalent JSON-LD framed document
* Validation
  * Validate a JTD using SHACL shapes
  * Validate a JTD using JSON schema (coming soon)
  * Validate a JTD according to the restrictions specified in the standard (coming soon)

The JTD is distributed as a Maven dependency easy to install and use.

#### Support or Contact

JTD was created by [Andrea Cimmino](https://scholar.google.es/citations?user=_6U9WMcAAAAJ&hl=es&oi=ao) (cimmino@fi.upm.es) and [Raúl García Castro](http://garcia-castro.com/) (rgarcia@fi.upm.es) at the [Universidad Politécnica de Madrid](https://www.upm.es/) in the research group [Ontology Engineering Group](https://oeg.fi.upm.es/).
