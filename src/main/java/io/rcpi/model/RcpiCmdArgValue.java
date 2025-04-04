/**
 * Created Aug 27, 2024
 */
package io.rcpi.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * @author robert.ilardi
 */

public class RcpiCmdArgValue implements Serializable {

  private Object value;

  public RcpiCmdArgValue() {}

  // Start Scalar Type Setters ---------------->

  public void setShort(Short value) {
    this.value = value;
  }

  public void setByte(Byte value) {
    this.value = value;
  }

  public void setCharacter(Character value) {
    this.value = value;
  }

  public void setBoolean(Boolean value) {
    this.value = value;
  }

  public void setInteger(Integer value) {
    this.value = value;
  }

  public void setLong(Long value) {
    this.value = value;
  }

  public void setFloat(Float value) {
    this.value = value;
  }

  public void setDouble(Double value) {
    this.value = value;
  }

  public void setString(String value) {
    this.value = value;
  }

  public void setObject(Object value) {
    this.value = value;
  }

  public void setDate(LocalDate value) {
    this.value = value;
  }

  public void setTime(LocalTime value) {
    this.value = value;
  }

  public void setDateTime(LocalDateTime value) {
    this.value = value;
  }

  public void setBinary(RcpiBinary value) {
    this.value = value;
  }

  // Start Array Type Setters ---------------->

  public void setShortArray(Short[] value) {
    this.value = value;
  }

  public void setByteArray(Byte[] value) {
    this.value = value;
  }

  public void setCharacterArray(Character[] value) {
    this.value = value;
  }

  public void setBooleanArray(Boolean[] value) {
    this.value = value;
  }

  public void setIntegerArray(Integer[] value) {
    this.value = value;
  }

  public void setLongArray(Long[] value) {
    this.value = value;
  }

  public void setFloatArray(Float[] value) {
    this.value = value;
  }

  public void setDoubleArray(Double[] value) {
    this.value = value;
  }

  public void setStringArray(String[] value) {
    this.value = value;
  }

  public void setObjectArray(Object[] value) {
    this.value = value;
  }

  public void setDateArray(LocalDate[] value) {
    this.value = value;
  }

  public void setTimeArray(LocalTime[] value) {
    this.value = value;
  }

  public void setDateTimeArray(LocalDateTime[] value) {
    this.value = value;
  }

  public void setBinaryArray(RcpiBinary[] value) {
    this.value = value;
  }

  // Start List Type Setters ---------------->

  public void setShortList(List<Short> value) {
    this.value = value;
  }

  public void setByteList(List<Byte> value) {
    this.value = value;
  }

  public void setCharacterList(List<Character> value) {
    this.value = value;
  }

  public void setBooleanList(List<Boolean> value) {
    this.value = value;
  }

  public void setIntegerList(List<Integer> value) {
    this.value = value;
  }

  public void setLongList(List<Long> value) {
    this.value = value;
  }

  public void setFloatList(List<Float> value) {
    this.value = value;
  }

  public void setDoubleList(List<Double> value) {
    this.value = value;
  }

  public void setStringList(List<String> value) {
    this.value = value;
  }

  public void setObjectList(List<Object> value) {
    this.value = value;
  }

  public void setDateList(List<LocalDate> value) {
    this.value = value;
  }

  public void setTimeList(List<LocalTime> value) {
    this.value = value;
  }

  public void setDateTimeList(List<LocalDateTime> value) {
    this.value = value;
  }

  public void setBinaryList(List<RcpiBinary> value) {
    this.value = value;
  }

}
