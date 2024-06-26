package org.babyfish.jimmer.sql.dto;

import org.babyfish.jimmer.sql.common.AbstractQueryTest;
import org.babyfish.jimmer.sql.model.TreeNode;
import org.babyfish.jimmer.sql.model.TreeNodeDraft;
import org.babyfish.jimmer.sql.model.TreeNodeTable;
import org.babyfish.jimmer.sql.model.dto.RecursiveTree;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

public class RecursiveTreeTest extends AbstractQueryTest {

    @Test
    public void testDtoToEntity() {
        RecursiveTree tree = root(
                "food",
                tree(
                        "drinks",
                        tree("cocacola"),
                        tree("fenta")
                )
        );
        assertContentEquals(
                "{" +
                        "--->\"name\":\"food\"," +
                        "--->\"childNodes\":[" +
                        "--->--->{" +
                        "--->--->--->\"name\":\"drinks\"," +
                        "--->--->--->\"childNodes\":[" +
                        "--->--->--->--->{" +
                        "--->--->--->--->--->\"name\":\"cocacola\"," +
                        "--->--->--->--->--->\"childNodes\":[]" +
                        "--->--->--->--->},{" +
                        "--->--->--->--->--->\"name\":\"fenta\"," +
                        "--->--->--->--->--->\"childNodes\":[]" +
                        "--->--->--->--->}" +
                        "--->--->--->]" +
                        "--->--->}" +
                        "--->]" +
                        "}",
                tree.toEntity().toString()
        );
    }

    @Test
    public void testEntityToDto() {
        TreeNode treeNode = TreeNodeDraft.$.produce(food -> {
           food.setName("food").addIntoChildNodes(drinks -> {
               drinks.setName("drinks");
               drinks.addIntoChildNodes(cocacola -> {
                   cocacola.setName("Cococla");
               });
               drinks.addIntoChildNodes(fenta -> {
                   fenta.setName("Fenta");
               });
           });
        });
        assertContentEquals(
                "RecursiveTree(" +
                        "--->name=food, " +
                        "--->childNodes=[" +
                        "--->--->RecursiveTree(" +
                        "--->--->--->name=drinks, " +
                        "--->--->--->childNodes=[" +
                        "--->--->--->--->RecursiveTree(" +
                        "--->--->--->--->--->name=Cococla, " +
                        "--->--->--->--->--->childNodes=null" +
                        "--->--->--->--->), " +
                        "--->--->--->--->RecursiveTree(" +
                        "--->--->--->--->--->name=Fenta, " +
                        "--->--->--->--->--->childNodes=null" +
                        "--->--->--->--->)" +
                        "--->--->--->]" +
                        "--->--->)" +
                        "--->]" +
                        ")",
                new RecursiveTree(treeNode)
        );
    }

    @Test
    public void testEntityToDto2() {
        TreeNode treeNode = TreeNodeDraft.$.produce(food -> {
            food.setName("food").addIntoChildNodes(drinks -> {
                drinks.setName("drinks");
                drinks.addIntoChildNodes(cocacola -> {
                    cocacola.setName("Cococla");
                    cocacola.setChildNodes(Collections.emptyList());
                });
                drinks.addIntoChildNodes(fenta -> {
                    fenta.setName("Fenta");
                    fenta.setChildNodes(Collections.emptyList());
                });
            });
        });
        assertContentEquals(
                "RecursiveTree(" +
                        "--->name=food, " +
                        "--->childNodes=[" +
                        "--->--->RecursiveTree(" +
                        "--->--->--->name=drinks, " +
                        "--->--->--->childNodes=[" +
                        "--->--->--->--->RecursiveTree(" +
                        "--->--->--->--->--->name=Cococla, " +
                        "--->--->--->--->--->childNodes=[]" +
                        "--->--->--->--->), " +
                        "--->--->--->--->RecursiveTree(" +
                        "--->--->--->--->--->name=Fenta, " +
                        "--->--->--->--->--->childNodes=[]" +
                        "--->--->--->--->)" +
                        "--->--->--->]" +
                        "--->--->)" +
                        "--->]" +
                        ")",
                new RecursiveTree(treeNode)
        );
    }

    @Test
    public void findFindTree() {

        TreeNodeTable table = TreeNodeTable.$;

        executeAndExpect(
                getSqlClient()
                        .createQuery(table)
                        .where(table.parentId().isNull())
                        .select(table.fetch(RecursiveTree.class)),
                ctx -> {
                    ctx.sql(
                            "select tb_1_.NODE_ID, tb_1_.NAME " +
                                    "from TREE_NODE tb_1_ " +
                                    "where tb_1_.PARENT_ID is null"
                    );
                    ctx.statement(1).sql(
                            "select tb_1_.NODE_ID, tb_1_.NAME " +
                                    "from TREE_NODE tb_1_ " +
                                    "where tb_1_.PARENT_ID = ? " +
                                    "order by tb_1_.NODE_ID asc"
                    );
                    ctx.statement(2).sql(
                            "select tb_1_.PARENT_ID, tb_1_.NODE_ID, tb_1_.NAME " +
                                    "from TREE_NODE tb_1_ " +
                                    "where tb_1_.PARENT_ID in (?, ?) " +
                                    "order by tb_1_.NODE_ID asc"
                    );
                    ctx.statement(3).sql(
                            "select tb_1_.PARENT_ID, tb_1_.NODE_ID, tb_1_.NAME " +
                                    "from TREE_NODE tb_1_ " +
                                    "where tb_1_.PARENT_ID in (?, ?, ?, ?) " +
                                    "order by tb_1_.NODE_ID asc"
                    );
                    ctx.statement(4).sql(
                            "select tb_1_.PARENT_ID, tb_1_.NODE_ID, tb_1_.NAME " +
                                    "from TREE_NODE tb_1_ " +
                                    "where tb_1_.PARENT_ID in (?, ?, ?, ?, ?, ?, ?, ?) " +
                                    "order by tb_1_.NODE_ID asc"
                    );
                    ctx.statement(5).sql(
                            "select tb_1_.PARENT_ID, tb_1_.NODE_ID, tb_1_.NAME " +
                                    "from TREE_NODE tb_1_ " +
                                    "where tb_1_.PARENT_ID in (?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                                    "order by tb_1_.NODE_ID asc"
                    );
                    ctx.rows(
                            "[" +
                                    "--->{" +
                                    "--->--->\"name\":\"Home\"," +
                                    "--->--->\"childNodes\":[" +
                                    "--->--->--->{" +
                                    "--->--->--->--->\"name\":\"Food\"," +
                                    "--->--->--->--->\"childNodes\":[" +
                                    "--->--->--->--->--->{" +
                                    "--->--->--->--->--->--->\"name\":\"Drinks\"," +
                                    "--->--->--->--->--->--->\"childNodes\":[" +
                                    "--->--->--->--->--->--->--->{" +
                                    "--->--->--->--->--->--->--->--->\"name\":\"Coca Cola\"," +
                                    "--->--->--->--->--->--->--->--->\"childNodes\":[" +
                                    "--->--->--->--->--->--->--->--->]" +
                                    "--->--->--->--->--->--->--->}," +
                                    "--->--->--->--->--->--->--->{" +
                                    "--->--->--->--->--->--->--->--->\"name\":\"Fanta\"," +
                                    "--->--->--->--->--->--->--->--->\"childNodes\":[" +
                                    "--->--->--->--->--->--->--->--->]" +
                                    "--->--->--->--->--->--->--->}" +
                                    "--->--->--->--->--->--->]" +
                                    "--->--->--->--->--->}," +
                                    "--->--->--->--->--->{" +
                                    "--->--->--->--->--->--->\"name\":\"Bread\"," +
                                    "--->--->--->--->--->--->\"childNodes\":[" +
                                    "--->--->--->--->--->--->--->{" +
                                    "--->--->--->--->--->--->--->--->\"name\":\"Baguette\"," +
                                    "--->--->--->--->--->--->--->--->\"childNodes\":[" +
                                    "--->--->--->--->--->--->--->--->]" +
                                    "--->--->--->--->--->--->--->}," +
                                    "--->--->--->--->--->--->--->{" +
                                    "--->--->--->--->--->--->--->--->\"name\":\"Ciabatta\"," +
                                    "--->--->--->--->--->--->--->--->\"childNodes\":[" +
                                    "--->--->--->--->--->--->--->--->]" +
                                    "--->--->--->--->--->--->--->}" +
                                    "--->--->--->--->--->--->]" +
                                    "--->--->--->--->--->}" +
                                    "--->--->--->--->]" +
                                    "--->--->--->}," +
                                    "--->--->--->{" +
                                    "--->--->--->--->\"name\":\"Clothing\"," +
                                    "--->--->--->--->\"childNodes\":[" +
                                    "--->--->--->--->--->{" +
                                    "--->--->--->--->--->--->\"name\":\"Woman\"," +
                                    "--->--->--->--->--->--->\"childNodes\":[" +
                                    "--->--->--->--->--->--->--->{" +
                                    "--->--->--->--->--->--->--->--->\"name\":\"Casual wear\"," +
                                    "--->--->--->--->--->--->--->--->\"childNodes\":[" +
                                    "--->--->--->--->--->--->--->--->--->{" +
                                    "--->--->--->--->--->--->--->--->--->--->\"name\":\"Dress\"," +
                                    "--->--->--->--->--->--->--->--->--->--->\"childNodes\":[" +
                                    "--->--->--->--->--->--->--->--->--->--->]" +
                                    "--->--->--->--->--->--->--->--->--->}," +
                                    "--->--->--->--->--->--->--->--->--->{" +
                                    "--->--->--->--->--->--->--->--->--->--->\"name\":\"Miniskirt\"," +
                                    "--->--->--->--->--->--->--->--->--->--->\"childNodes\":[" +
                                    "--->--->--->--->--->--->--->--->--->--->]" +
                                    "--->--->--->--->--->--->--->--->--->}," +
                                    "--->--->--->--->--->--->--->--->--->{" +
                                    "--->--->--->--->--->--->--->--->--->--->\"name\":\"Jeans\"," +
                                    "--->--->--->--->--->--->--->--->--->--->\"childNodes\":[" +
                                    "--->--->--->--->--->--->--->--->--->--->]" +
                                    "--->--->--->--->--->--->--->--->--->}" +
                                    "--->--->--->--->--->--->--->--->]" +
                                    "--->--->--->--->--->--->--->}," +
                                    "--->--->--->--->--->--->--->{" +
                                    "--->--->--->--->--->--->--->--->\"name\":\"Formal wear\"," +
                                    "--->--->--->--->--->--->--->--->\"childNodes\":[" +
                                    "--->--->--->--->--->--->--->--->--->{" +
                                    "--->--->--->--->--->--->--->--->--->--->\"name\":\"Suit\"," +
                                    "--->--->--->--->--->--->--->--->--->--->\"childNodes\":[" +
                                    "--->--->--->--->--->--->--->--->--->--->]" +
                                    "--->--->--->--->--->--->--->--->--->}," +
                                    "--->--->--->--->--->--->--->--->--->{" +
                                    "--->--->--->--->--->--->--->--->--->--->\"name\":\"Shirt\"," +
                                    "--->--->--->--->--->--->--->--->--->--->\"childNodes\":[" +
                                    "--->--->--->--->--->--->--->--->--->--->]" +
                                    "--->--->--->--->--->--->--->--->--->}" +
                                    "--->--->--->--->--->--->--->--->]" +
                                    "--->--->--->--->--->--->--->}" +
                                    "--->--->--->--->--->--->]" +
                                    "--->--->--->--->--->}," +
                                    "--->--->--->--->--->{" +
                                    "--->--->--->--->--->--->\"name\":\"Man\"," +
                                    "--->--->--->--->--->--->\"childNodes\":[" +
                                    "--->--->--->--->--->--->--->{" +
                                    "--->--->--->--->--->--->--->--->\"name\":\"Casual wear\"," +
                                    "--->--->--->--->--->--->--->--->\"childNodes\":[" +
                                    "--->--->--->--->--->--->--->--->--->{" +
                                    "--->--->--->--->--->--->--->--->--->--->\"name\":\"Jacket\"," +
                                    "--->--->--->--->--->--->--->--->--->--->\"childNodes\":[" +
                                    "--->--->--->--->--->--->--->--->--->--->]" +
                                    "--->--->--->--->--->--->--->--->--->}," +
                                    "--->--->--->--->--->--->--->--->--->{" +
                                    "--->--->--->--->--->--->--->--->--->--->\"name\":\"Jeans\"," +
                                    "--->--->--->--->--->--->--->--->--->--->\"childNodes\":[" +
                                    "--->--->--->--->--->--->--->--->--->--->]" +
                                    "--->--->--->--->--->--->--->--->--->}" +
                                    "--->--->--->--->--->--->--->--->]" +
                                    "--->--->--->--->--->--->--->}," +
                                    "--->--->--->--->--->--->--->{" +
                                    "--->--->--->--->--->--->--->--->\"name\":\"Formal wear\"," +
                                    "--->--->--->--->--->--->--->--->\"childNodes\":[" +
                                    "--->--->--->--->--->--->--->--->--->{" +
                                    "--->--->--->--->--->--->--->--->--->--->\"name\":\"Suit\"," +
                                    "--->--->--->--->--->--->--->--->--->--->\"childNodes\":[" +
                                    "--->--->--->--->--->--->--->--->--->--->]" +
                                    "--->--->--->--->--->--->--->--->--->}," +
                                    "--->--->--->--->--->--->--->--->--->{" +
                                    "--->--->--->--->--->--->--->--->--->--->\"name\":\"Shirt\"," +
                                    "--->--->--->--->--->--->--->--->--->--->\"childNodes\":[" +
                                    "--->--->--->--->--->--->--->--->--->--->]" +
                                    "--->--->--->--->--->--->--->--->--->}" +
                                    "--->--->--->--->--->--->--->--->]" +
                                    "--->--->--->--->--->--->--->}" +
                                    "--->--->--->--->--->--->]" +
                                    "--->--->--->--->--->}" +
                                    "--->--->--->--->]" +
                                    "--->--->--->}" +
                                    "--->--->]" +
                                    "--->}" +
                                    "]--->"
                    );
                }
        );
    }

    private static RecursiveTree root(String name, RecursiveTree ...childNodes) {
        RecursiveTree tree = new RecursiveTree();
        tree.setName(name);
        if (childNodes.length > 0) {
            tree.setChildNodes(Arrays.asList(childNodes));
        }
        return tree;
    }

    private static RecursiveTree tree(String name, RecursiveTree ...childNodes) {
        RecursiveTree tree = new RecursiveTree();
        tree.setName(name);
        if (childNodes.length > 0) {
            tree.setChildNodes(Arrays.asList(childNodes));
        }
        return tree;
    }
}
