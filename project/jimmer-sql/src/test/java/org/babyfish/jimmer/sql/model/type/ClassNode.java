package org.babyfish.jimmer.sql.model.type;

import org.babyfish.jimmer.sql.Entity;

@Entity
public interface ClassNode extends Annotated {

    String typeName();
}
