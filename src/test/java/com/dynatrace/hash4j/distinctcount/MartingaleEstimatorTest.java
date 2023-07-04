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
package com.dynatrace.hash4j.distinctcount;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MartingaleEstimatorTest {

  @Test
  void testToString() {
    assertThat(new MartingaleEstimator())
        .hasToString("MartingaleEstimator{distinctCountEstimate=0.0, stateChangeProbability=1.0}");
    assertThat(new MartingaleEstimator(2, 0.25))
        .hasToString("MartingaleEstimator{distinctCountEstimate=2.0, stateChangeProbability=0.25}");
  }

  @Test
  void testConstructorWithNegativeZeroStateChangeProbability() {
    MartingaleEstimator estimator = new MartingaleEstimator(0, -0.0);
    estimator.stateChanged(0.5);
    assertThat(estimator.getDistinctCountEstimate()).isPositive().isInfinite();
  }

  @Test
  void testConstructorWithIllegalArguments() {
    assertThatIllegalArgumentException().isThrownBy(() -> new MartingaleEstimator(-1, 1));
    assertThatIllegalArgumentException().isThrownBy(() -> new MartingaleEstimator(Double.NaN, 1));
    assertThatIllegalArgumentException()
        .isThrownBy(() -> new MartingaleEstimator(Double.NEGATIVE_INFINITY, 1));
    assertThatIllegalArgumentException().isThrownBy(() -> new MartingaleEstimator(0, 2));
    assertThatIllegalArgumentException().isThrownBy(() -> new MartingaleEstimator(0, -1));
  }

  @Test
  void testBasicUsage() {
    MartingaleEstimator estimator = new MartingaleEstimator();
    assertThat(estimator.getDistinctCountEstimate()).isZero();
    assertThat(estimator.getStateChangeProbability()).isOne();
    for (int i = 1; i <= 100; ++i) {
      estimator.stateChanged(Math.pow(0.5, i));
      assertThat(estimator.getStateChangeProbability()).isEqualTo(Math.pow(0.5, i));
      assertThat(estimator.getDistinctCountEstimate()).isEqualTo(Math.pow(2., i) - 1.);
    }
  }

  @Test
  void testSet() {
    double distinctCountEstimate = 23478952;
    double stateChangeProbability = 0.823568;

    MartingaleEstimator martingaleEstimator = new MartingaleEstimator();
    martingaleEstimator.set(distinctCountEstimate, stateChangeProbability);

    assertThat(martingaleEstimator.getStateChangeProbability()).isEqualTo(stateChangeProbability);
    assertThat(martingaleEstimator.getDistinctCountEstimate()).isEqualTo(distinctCountEstimate);
  }

  @Test
  void testReset() {
    double distinctCountEstimate = 23478952;
    double stateChangeProbability = 0.823568;

    MartingaleEstimator martingaleEstimator =
        new MartingaleEstimator(distinctCountEstimate, stateChangeProbability);
    martingaleEstimator.reset();

    assertThat(martingaleEstimator.getStateChangeProbability()).isEqualTo(1.);
    assertThat(martingaleEstimator.getDistinctCountEstimate()).isEqualTo(0.);
  }

  @Test
  void testSetArguments() {
    MartingaleEstimator martingaleEstimator = new MartingaleEstimator();
    assertThatIllegalArgumentException().isThrownBy(() -> martingaleEstimator.set(-2, 0.5));
    assertThatIllegalArgumentException().isThrownBy(() -> martingaleEstimator.set(1, 1.5));
    assertThatNoException().isThrownBy(() -> martingaleEstimator.set(2, -0.0));
    assertThatIllegalArgumentException().isThrownBy(() -> martingaleEstimator.set(2, -0.1));
  }
}
