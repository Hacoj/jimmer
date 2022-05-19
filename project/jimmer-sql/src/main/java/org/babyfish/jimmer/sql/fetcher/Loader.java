package org.babyfish.jimmer.sql.fetcher;

import org.babyfish.jimmer.lang.OldChain;
import org.babyfish.jimmer.sql.ast.table.Table;

public interface Loader<E, T extends Table<E>> {

    @OldChain
    Loader<E, T> filter(Filter<E, T> filter);

    @OldChain
    Loader<E, T> batch(int size);
}
