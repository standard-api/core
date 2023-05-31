package ai.stapi.test.schemaintegration;

import java.lang.annotation.*;

@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface StructureDefinitionScope {

  String[] value();
}

