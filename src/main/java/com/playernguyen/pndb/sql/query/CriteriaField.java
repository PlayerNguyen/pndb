package com.playernguyen.pndb.sql.query;

/**
 * A criteria field is a way comparisons in a criteria string. Which represents by a fiend name, operator,
 * and a question symbol (aka dispatched data)<br>
 * <b>For example (with question symbol is data):</b> <br>
 * <ul>
 *     <li>fieldName = ? (fieldName is equals to ?)</li>
 *     <li>fieldName >= ? (fieldName is more than ?)</li>
 *     <li>fieldName <= ? (fieldName is less than ?)</li>
 *     <li>fieldName LIKE ? (fieldName is less than ?)</li>
 *     <li>...</li>
 * </ul>
 */
public class CriteriaField {
    private final String fieldName;
    private final CriteriaFieldOperator type;


    public CriteriaField(String fieldName, CriteriaFieldOperator type) {
        this.fieldName = fieldName;
        this.type = type;
    }

    /**
     * A field name represents to a columns name in SQL.
     *
     * @return a field name
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * A criteria field type, which contains operator to build a right criteria
     *
     * @return a type
     */
    public CriteriaFieldOperator getType() {
        return type;
    }

    /**
     * Generate new field with equal operator
     *
     * @param name a field name
     * @return a criteria field which was just generated
     */
    public static CriteriaField equal(String name) {
        return new CriteriaField(name, CriteriaFieldOperator.EQUAL);
    }

    /**
     * Generate new field with more than operator
     *
     * @param name a field name
     * @return a criteria field which was just generated
     */
    public static CriteriaField moreThan(String name) {
        return new CriteriaField(name, CriteriaFieldOperator.MORE_THAN);
    }

    /**
     * Generate new field with less than operator
     *
     * @param name a field name
     * @return a criteria field which was just generated
     */
    public static CriteriaField lessThan(String name) {
        return new CriteriaField(name, CriteriaFieldOperator.LESS_THAN);
    }

    /**
     * Generate new field with more than or equal operator
     *
     * @param name a field name
     * @return a criteria field which was just generated
     */
    public static CriteriaField moreThanOrEqual(String name) {
        return new CriteriaField(name, CriteriaFieldOperator.MORE_OR_EQUAL);
    }

    /**
     * Generate new field with less than or equal operator
     *
     * @param name a field name
     * @return a criteria field which was just generated
     */
    public static CriteriaField lessThanOrEqual(String name) {
        return new CriteriaField(name, CriteriaFieldOperator.LESS_OR_EQUAL);
    }

    /**
     * Generate new field with not equal operator
     *
     * @param name a field name
     * @return a criteria field which was just generated
     */
    public static CriteriaField notEqual(String name) {
        return new CriteriaField(name, CriteriaFieldOperator.NOT_EQUAL);
    }

    /**
     * Generate new field with not more than operator
     *
     * @param name a field name
     * @return a criteria field which was just generated
     */
    public static CriteriaField notMoreThan(String name) {
        return new CriteriaField(name, CriteriaFieldOperator.NOT_MORE_THAN);
    }

    /**
     * Generate new field with not less than operator
     *
     * @param name a field name
     * @return a criteria field which was just generated
     */
    public static CriteriaField notLessThan(String name) {
        return new CriteriaField(name, CriteriaFieldOperator.NOT_LESS_THAN);
    }

    /**
     * Generate new field with like operator
     *
     * @param name a field name
     * @return a criteria field which was just generated
     */
    public static CriteriaField like(String name) {
        return new CriteriaField(name, CriteriaFieldOperator.LIKE);
    }

}
