/**
 * Created Aug 10, 2016
 */
package com.ilardi.experiments.dbms.query.sql;

/**
 * @author rilardi
 *
 */

public class SqlCriterion extends BaseSqlElement {

  protected LeftOperand leftOperand;

  protected Operator operator;

  protected RightOperand rightOperand;

  protected BooleanOperator boolOperator;

  public SqlCriterion() {
    super();
  }

  public LeftOperand getLeftOperand() {
    return leftOperand;
  }

  public void setLeftOperand(LeftOperand leftOperand) {
    this.leftOperand = leftOperand;
  }

  public Operator getOperator() {
    return operator;
  }

  public void setOperator(Operator operator) {
    this.operator = operator;
  }

  public RightOperand getRightOperand() {
    return rightOperand;
  }

  public void setRightOperand(RightOperand rightOperand) {
    this.rightOperand = rightOperand;
  }

  public BooleanOperator getBoolOperator() {
    return boolOperator;
  }

  public void setBoolOperator(BooleanOperator boolOperator) {
    this.boolOperator = boolOperator;
  }

}
