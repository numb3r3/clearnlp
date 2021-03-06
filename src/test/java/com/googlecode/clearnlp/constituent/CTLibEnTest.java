/**
* Copyright (c) 2009-2012, Regents of the University of Colorado
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
package com.googlecode.clearnlp.constituent;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.googlecode.clearnlp.constituent.CTLibEn;
import com.googlecode.clearnlp.constituent.CTReader;
import com.googlecode.clearnlp.constituent.CTTree;
import com.googlecode.clearnlp.util.UTInput;


/** @author Jinho D. Choi ({@code choijd@colorado.edu}) */
public class CTLibEnTest
{
	@Test
	public void testCTLibEn()
	{
		String filename = "src/test/resources/constituent/CTLibEnTest.parse";
		CTReader reader = new CTReader(UTInput.createBufferedFileReader(filename));
		CTTree   tree;

		tree = reader.nextTree();	CTLibEn.preprocessTree(tree);	tree.setPBLocs();
		assertEquals("3:1" , tree.getNode( 7, 0).getAntecedent().getPBLoc().toString());
		tree = reader.nextTree();	CTLibEn.preprocessTree(tree);	tree.setPBLocs();
		assertEquals("9:0" , tree.getNode(11, 0).getAntecedent().getPBLoc().toString());
		tree = reader.nextTree();	CTLibEn.preprocessTree(tree);	tree.setPBLocs();
		assertEquals("6:1" , tree.getNode(11, 0).getAntecedent().getPBLoc().toString());
		tree = reader.nextTree();	CTLibEn.preprocessTree(tree);	tree.setPBLocs();
		assertEquals("33:1", tree.getNode(36, 0).getAntecedent().getPBLoc().toString());
		tree = reader.nextTree();	CTLibEn.preprocessTree(tree);	tree.setPBLocs();
		assertEquals("2:1" , tree.getNode( 5, 0).getAntecedent().getPBLoc().toString());
		tree = reader.nextTree();	CTLibEn.preprocessTree(tree);	tree.setPBLocs();
		assertEquals("6:4" , tree.getNode( 1, 0).getAntecedent().getPBLoc().toString());
		tree = reader.nextTree();	CTLibEn.preprocessTree(tree);	tree.setPBLocs();
		assertEquals("8:1" , tree.getNode(20, 0).getAntecedent().getPBLoc().toString());
		tree = reader.nextTree();	CTLibEn.preprocessTree(tree);	tree.setPBLocs();
		assertEquals("11:1", tree.getNode(13, 0).getAntecedent().getPBLoc().toString());
		tree = reader.nextTree();	CTLibEn.preprocessTree(tree);	tree.setPBLocs();
		assertEquals("9:1" , tree.getNode(11, 0).getAntecedent().getPBLoc().toString());
		tree = reader.nextTree();	CTLibEn.preprocessTree(tree);	tree.setPBLocs();
		assertEquals("22:1", tree.getNode(23, 0).getAntecedent().getPBLoc().toString());
		tree = reader.nextTree();	CTLibEn.preprocessTree(tree);	tree.setPBLocs();
		assertEquals("6:1" , tree.getNode( 8, 0).getAntecedent().getPBLoc().toString());
		tree = reader.nextTree();	CTLibEn.preprocessTree(tree);	tree.setPBLocs();
		assertEquals("0:1" , tree.getNode( 3, 0).getAntecedent().getPBLoc().toString());
		tree = reader.nextTree();	CTLibEn.preprocessTree(tree);	tree.setPBLocs();
		assertEquals("8:1" , tree.getNode(11, 0).getAntecedent().getPBLoc().toString());
		tree = reader.nextTree();	CTLibEn.preprocessTree(tree);	tree.setPBLocs();
		assertEquals("6:1" , tree.getNode( 9, 0).getAntecedent().getPBLoc().toString());
		tree = reader.nextTree();	CTLibEn.preprocessTree(tree);	tree.setPBLocs();
		assertEquals("10:1", tree.getNode(14, 0).getAntecedent().getPBLoc().toString());
		
		reader.close();
	}
}