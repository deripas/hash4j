/*
 * Copyright 2022-2023 Dynatrace LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dynatrace.hash4j.hashing;

/** Represents a 128-bit hash value. */
public final class HashValue128 {
  private final long mostSignificantBits;
  private final long leastSignificantBits;

  /**
   * Constructor.
   *
   * @param mostSignificantBits the 64 most significant bits of the hash value
   * @param leastSignificantBits the 64 least significant bits of the hash value
   */
  public HashValue128(long mostSignificantBits, long leastSignificantBits) {
    this.mostSignificantBits = mostSignificantBits;
    this.leastSignificantBits = leastSignificantBits;
  }

  /**
   * Returns the 32 least significant bits of the hash value.
   *
   * @return a 32-bit hash value
   */
  public int getAsInt() {
    return (int) getLeastSignificantBits();
  }

  /**
   * Returns the 64 most significant bits of the hash value.
   *
   * @return a 64-bit hash value
   */
  public long getMostSignificantBits() {
    return mostSignificantBits;
  }

  /**
   * Returns the 64 least significant bits of the hash value.
   *
   * <p>Equivalent to {@link #getAsLong()}.
   *
   * @return a 64-bit hash value
   */
  public long getLeastSignificantBits() {
    return leastSignificantBits;
  }

  /**
   * Returns the 64 least significant bits of the hash value.
   *
   * @return a 64-bit hash value
   */
  public long getAsLong() {
    return getLeastSignificantBits();
  }

  @Override
  public int hashCode() {
    return getAsInt();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    HashValue128 other = (HashValue128) obj;
    if (leastSignificantBits != other.leastSignificantBits) {
      return false;
    }
    if (mostSignificantBits != other.mostSignificantBits) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "0x" + HashValues.toHexString(this);
  }

  /**
   * Returns this hash value as byte array.
   *
   * @return a byte array of length 16
   */
  public byte[] toByteArray() {
    return HashValues.toByteArray(this);
  }
}
