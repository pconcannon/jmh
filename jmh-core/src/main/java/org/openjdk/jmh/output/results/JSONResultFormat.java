/*
 * Copyright (c) 2005, 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package org.openjdk.jmh.output.results;

import org.openjdk.jmh.logic.results.Result;
import org.openjdk.jmh.logic.results.RunResult;
import org.openjdk.jmh.runner.BenchmarkRecord;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class JSONResultFormat implements ResultFormat {

    private final String output;

    public JSONResultFormat(String output) {
        this.output = output;
    }

    @Override
    public void writeOut(Map<BenchmarkRecord, RunResult> results) {
        PrintWriter pw = null;
        try  {
            pw = new PrintWriter(output);

            boolean first = true;

            pw.println("[");
            for (BenchmarkRecord br : results.keySet()) {
                if (first) {
                    first = false;
                } else {
                    pw.println(",");
                }

                RunResult runResult = results.get(br);

                pw.println("{");
                pw.println("  'benchmark' : '" + br.getUsername() + "',");
                pw.println("  'mode' : '" + br.getMode().shortLabel() + "',");
                pw.println("  'threads' : " + runResult.getThreads() + ",");
                pw.println("  'iterations' : " + runResult.getIterationCount() + ",");
                pw.println("  'iterationTime' : '" + runResult.getTime() + "',");
                pw.println("  'primaryMetric' : {");
                pw.println("    'score' : " + runResult.getPrimaryResult().getScore() + ",");
                pw.println("    'scoreUnit' : '" + runResult.getPrimaryResult().getScoreUnit() + "',");
                pw.println("    'raw' : [");
                for (Result r : runResult.getRawPrimaryResults()) {
                    pw.println("      " + r.getScore());
                }
                pw.println("    ]");
                pw.println("}");
            }
            pw.println("]");

        } catch (IOException e) {
            throw new IllegalStateException(e);
        } finally {
            if (pw != null) {
                pw.close();
            }
        }

    }

}
