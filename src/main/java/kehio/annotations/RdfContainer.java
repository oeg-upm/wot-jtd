package kehio.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
public @interface RdfContainer {
	String[] ignore() default {};
	RdfUrlMap[] prefixes() default {};
	RdfUrlMap[] aliases(); // if property is present in alias, overrides the ignore properties 
	String[] identifiers();
}
