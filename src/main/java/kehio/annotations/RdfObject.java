package kehio.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
public @interface RdfObject {
	String value() default "";
	String base() default "";
	RdfUrlMap[] aliases() default {};
	boolean strict() default false; // if true only matches aliases 
}
