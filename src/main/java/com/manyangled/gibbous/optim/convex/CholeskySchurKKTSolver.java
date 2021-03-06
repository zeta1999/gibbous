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

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.CholeskyDecomposition;
import org.apache.commons.math3.linear.DecompositionSolver;

/**
 * Solves KKT conditions for {@link NewtonOptimizer}, using Schur block factorization and
 * Cholesky decomposition.
 * <p>
 * Implements (Algorithm 10.3) and Step 1 of (Algorithm 9.5) from
 * Convex Optimization, Boyd and Vandenberghe, Cambridge University Press, 2008.
 * <p>
 * This is currently the default {@link KKTSolver} for {@link NewtonOptimizer}.
 * <p>
 * See also {@link SVDSchurKKTSolver}
 */
public class CholeskySchurKKTSolver extends KKTSolver {
    // step 1 of algorithm 9.5
    public KKTSolution solve(final RealMatrix H, final RealVector g) {
        DecompositionSolver dsH = (new CholeskyDecomposition(H)).getSolver();
        RealVector v = dsH.solve(g);
        double lsq = g.dotProduct(v);
        v.mapMultiplyToSelf(-1.0);
        return new KKTSolution(v, lsq);
    }

    // Algorithm 10.3
    public KKTSolution solve(
        final RealMatrix H,
        final RealMatrix A, final RealMatrix AT,
        final RealVector g, final RealVector h) {
        DecompositionSolver dsH = (new CholeskyDecomposition(H)).getSolver();
        RealMatrix m1 = dsH.solve(AT);
        RealVector v1 = dsH.solve(g);
        RealMatrix S = A.multiply(m1); // -S relative to 10.3
        DecompositionSolver dsS = (new CholeskyDecomposition(S)).getSolver();
        RealVector w = dsS.solve(h.subtract(A.operate(v1))); // both sides neg, so w same
        RealVector v = dsH.solve(g.add(AT.operate(w))); // this yields -v
        v.mapMultiplyToSelf(-1.0); // correct -v to +v
        return new KKTSolution(v, w);
    }
}
