package org.babyfish.jimmer.client.meta;

import java.util.List;
import java.util.Map;

public interface TypeDefinition {

    String getTypeName();

    boolean isImmutable();

    Map<String, Prop> getPropMap();

    List<TypeRef> getSuperTypes();

    Doc getDoc();
}
