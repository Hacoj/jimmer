package org.babyfish.jimmer.sql.model.inheritance;

import org.babyfish.jimmer.sql.Entity;
import org.babyfish.jimmer.sql.Id;

@Entity
public interface AdministratorMetadata extends AdministratorMetadataBase {

    @Id
    long getId();
}