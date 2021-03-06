/*
Copyright 2018 Erik Erlandson
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.manyangled.gibbous.optim.convex;

import org.apache.commons.math3.util.Pair;
import org.apache.commons.math3.optim.OptimizationData;
import org.apache.commons.math3.optim.ConvergenceChecker;
import org.apache.commons.math3.linear.RealVector;

/**
 * Represents a custom halting condition that will cause an optimization to
 * stop iterating if it returns true.
 * <p>
 * An example application is {@link ConvexOptimizer.NegChecker}, which halts
 * an optimization during search for a feasible point if such a point is 
 * discovered.
 * <p>
 * This class is intended primarily for internal use by library algorithms.
 * Use with caution.
 */
public class HaltingCondition implements OptimizationData {
    public final ConvergenceChecker<Pair<RealVector, Double> > checker;

    /**
     * Construct a halting condition from a {@link ConvergenceChecker}
     * @param checker the convergence checker to use as the test for halting condition
     */
    public HaltingCondition(ConvergenceChecker<Pair<RealVector, Double> > checker) {
        this.checker = checker;
    }
}
