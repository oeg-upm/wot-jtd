package kehio.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
public @interface RdfDatatype {
	String value() default "";
	String lang() default "";
	String datatype() default "";
	boolean sinkLang() default false;
	boolean sinkDatatype() default false;
}

