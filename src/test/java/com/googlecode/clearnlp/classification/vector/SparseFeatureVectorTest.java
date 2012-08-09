/**
* Copyright (c) 2011, Regents of the University of Colorado
* All rights reserved.
*
* Redistribution and use in source and binary forms, with or without
* modification, are permitted provided that the following conditions are met:
*
* Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
* Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
* Neither the name of the University of Colorado at Boulder nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
*
* THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
* AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
* IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
* ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
* LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
* CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
* SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
* INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
* CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
* ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
* POSSIBILITY OF SUCH DAMAGE.
*/
package com.googlecode.clearnlp.classification.vector;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import com.googlecode.clearnlp.classification.vector.SparseFeatureVector;

/** @author Jinho D. Choi ({@code choijd@colorado.edu}) */
public class SparseFeatureVectorTest
{
	@Test
	public void testSparseFeatureVector()
	{
		// features without weights
		SparseFeatureVector vector = new SparseFeatureVector();

		vector.addFeature(0);
		vector.addFeature("1");
		
		assertEquals(0, vector.getIndex(0));
		assertEquals(2, vector.size());
		assertEquals("0 1", vector.toString());
		
		// features with weights
		vector = new SparseFeatureVector(true);
		
		vector.addFeature(0, 0.1);
		vector.addFeature("1:0.2");
		
		assertEquals(0, vector.getIndex(0));
		assertEquals(true, 0.2 == vector.getWeight(1));
		assertEquals(2, vector.size());
		assertEquals("0:0.1 1:0.2", vector.toString());
	}
}
