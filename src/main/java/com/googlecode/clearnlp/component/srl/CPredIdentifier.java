/**
* Copyright 2012-2013 University of Massachusetts Amherst
* 
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* 
*   http://www.apache.org/licenses/LICENSE-2.0
*   
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.googlecode.clearnlp.component.srl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import com.googlecode.clearnlp.classification.model.AbstractModel;
import com.googlecode.clearnlp.classification.model.StringModel;
import com.googlecode.clearnlp.classification.prediction.StringPrediction;
import com.googlecode.clearnlp.classification.train.StringTrainSpace;
import com.googlecode.clearnlp.classification.vector.StringFeatureVector;
import com.googlecode.clearnlp.component.AbstractStatisticalComponent;
import com.googlecode.clearnlp.dependency.DEPArc;
import com.googlecode.clearnlp.dependency.DEPLib;
import com.googlecode.clearnlp.dependency.DEPNode;
import com.googlecode.clearnlp.dependency.DEPTree;
import com.googlecode.clearnlp.feature.xml.FtrToken;
import com.googlecode.clearnlp.feature.xml.JointFtrXml;
import com.googlecode.clearnlp.nlp.NLPLib;

/**
 * PropBank predicate identifier.
 * @since 1.3.0
 * @author Jinho D. Choi ({@code jdchoi77@gmail.com})
 */
public class CPredIdentifier extends AbstractStatisticalComponent
{
	private final String ENTRY_CONFIGURATION = NLPLib.MODE_PRED + NLPLib.ENTRY_CONFIGURATION;
	private final String ENTRY_FEATURE		 = NLPLib.MODE_PRED + NLPLib.ENTRY_FEATURE;
	private final String ENTRY_MODEL		 = NLPLib.MODE_PRED + NLPLib.ENTRY_MODEL;
	
	protected Boolean[] g_preds;
	protected int       i_pred;
	
//	====================================== CONSTRUCTORS ======================================

	/** Constructs a predicate identifier for collecting lexica. */
	public CPredIdentifier(JointFtrXml[] xmls)
	{
		super(xmls);
	}
		
	/** Constructs a predicate identifier for training. */
	public CPredIdentifier(JointFtrXml[] xmls, StringTrainSpace[] spaces, Object[] lexica)
	{
		super(xmls, spaces, lexica);
	}
	
	/** Constructs a predicate identifier for developing. */
	public CPredIdentifier(JointFtrXml[] xmls, StringModel[] models, Object[] lexica)
	{
		super(xmls, models, lexica);
	}
	
	/** Constructs a predicate identifier for decoding. */
	public CPredIdentifier(ZipInputStream in)
	{
		super(in);
	}
	
	protected void initLexia(Object[] lexica) {}
	
//	====================================== LOAD/SAVE MODELS ======================================
	
	@Override
	public void loadModels(ZipInputStream zin)
	{
		int fLen = ENTRY_FEATURE.length(), mLen = ENTRY_MODEL.length();
		f_xmls   = new JointFtrXml[1];
		s_models = null;
		ZipEntry zEntry;
		String   entry;
				
		try
		{
			while ((zEntry = zin.getNextEntry()) != null)
			{
				entry = zEntry.getName();
				
				if      (entry.equals(ENTRY_CONFIGURATION))
					loadDefaultConfiguration(zin);
				else if (entry.startsWith(ENTRY_FEATURE))
					loadFeatureTemplates(zin, Integer.parseInt(entry.substring(fLen)));
				else if (entry.startsWith(ENTRY_MODEL))
					loadStatisticalModels(zin, Integer.parseInt(entry.substring(mLen)));
			}		
		}
		catch (Exception e) {e.printStackTrace();}
	}

	@Override
	public void saveModels(ZipOutputStream zout)
	{
		try
		{
			saveDefaultConfiguration(zout, ENTRY_CONFIGURATION);
			saveFeatureTemplates    (zout, ENTRY_FEATURE);
			saveStatisticalModels   (zout, ENTRY_MODEL);
			zout.close();
		}
		catch (Exception e) {e.printStackTrace();}
	}

//	====================================== GETTERS AND SETTERS ======================================

	@Override
	public Object[] getLexica()
	{
		return null;
	}

//	====================================== PROCESS ======================================
	
	@Override
	public void countAccuracy(int[] counts)
	{
		int i, pTotal = 0, rTotal = 0, correct = 0;
		DEPNode node;
		
		for (i=1; i<t_size; i++)
		{
			node = d_tree.get(i);
			
			if (node.getFeat(DEPLib.FEAT_PB) != null)
			{
				pTotal++;
				if (g_preds[i])	correct++;
			}
			
			if (g_preds[i])
				rTotal++;
		}
		
		counts[0] += correct;
		counts[1] += pTotal;
		counts[2] += rTotal;
	}
	
	@Override
	public void process(DEPTree tree)
	{
		init(tree);
		identify();
	}
	
	/** Called by {@link CPredIdentifier#process(DEPTree)}. */
	protected void init(DEPTree tree)
	{
	 	d_tree = tree;
	 	t_size = tree.size();

	 	if (i_flag != FLAG_DECODE)
	 		g_preds = tree.getPredicates();
	 	
	 	tree.clearPredicates();
	 	tree.setDependents();
	}
	
	/** Called by {@link CPredIdentifier#process(DEPTree)}. */
	protected void identify()
	{
		DEPNode pred;
		String label;
		
		for (i_pred=1; i_pred<t_size; i_pred++)
		{
			pred = d_tree.get(i_pred);
			
			if (f_xmls[0].isPredicate(pred))
			{
				label = getLabel();
				
				if (label.equals(AbstractModel.LABEL_TRUE))
					pred.addFeat(DEPLib.FEAT_PB, pred.lemma+".XX");
			}
		}
	}
	
	/** Called by {@link CPredIdentifier#identify()}. */
	protected String getLabel()
 	 {
		StringFeatureVector vector = getFeatureVector(f_xmls[0]);
		String label = null;
		
		if (i_flag == FLAG_TRAIN)
		{
			label = getGoldLabel();
			s_spaces[0].addInstance(label, vector);
		}
		else if (i_flag == FLAG_DECODE || i_flag == FLAG_DEVELOP)
		{
			label = getAutoLabel(vector);
		}
		
		return label;
	}
	
	/** Called by {@link CPredIdentifier#getLabel()}. */
	private String getGoldLabel()
	{
		return g_preds[i_pred] ? AbstractModel.LABEL_TRUE : AbstractModel.LABEL_FALSE;
	}
	
	/** Called by {@link CPredIdentifier#getLabel()}. */
	private String getAutoLabel(StringFeatureVector vector)
	{
		StringPrediction p = s_models[0].predictBest(vector);
		return p.label;
	}
	
//	====================================== FEATURE EXTRACTION ======================================

	@Override
	protected String getField(FtrToken token)
	{
		DEPNode node = getNode(token);
		if (node == null)	return null;
		Matcher m;
		
		if (token.isField(JointFtrXml.F_FORM))
		{
			return node.form;
		}
		else if (token.isField(JointFtrXml.F_LEMMA))
		{
			return node.lemma;
		}
		else if (token.isField(JointFtrXml.F_POS))
		{
			return node.pos;
		}
		else if (token.isField(JointFtrXml.F_DEPREL))
		{
			return node.getLabel();
		}
		else if ((m = JointFtrXml.P_FEAT.matcher(token.field)).find())
		{
			return node.getFeat(m.group(1));
		}
		
		return null;
	}
	
	@Override
	protected String[] getFields(FtrToken token)
	{
		DEPNode node = getNode(token);
		if (node == null)	return null;
		
		if (token.isField(JointFtrXml.F_DEPREL_SET))
		{
			return getDeprelSet(node.getDependents());
		}
		
		return null;
	}
	
	private String[] getDeprelSet(List<DEPArc> deps)
	{
		if (deps.isEmpty())	return null;
		
		Set<String> set = new HashSet<String>();
		for (DEPArc arc : deps)	set.add(arc.getLabel());
		
		String[] fields = new String[set.size()];
		set.toArray(fields);
		
		return fields;		
	}
	
	private DEPNode getNode(FtrToken token)
	{
		DEPNode node = getNodeAux(token);
		if (node == null)	return null;
		
		if (token.relation != null)
		{
			     if (token.isRelation(JointFtrXml.R_H))	node = node.getHead();
			else if (token.isRelation(JointFtrXml.R_LMD))	node = node.getLeftMostDependent();
			else if (token.isRelation(JointFtrXml.R_RMD))	node = node.getRightMostDependent();
		}
		
		return node;
	}
	
	/** Called by {@link CPredIdentifier#getNode(FtrToken)}. */
	private DEPNode getNodeAux(FtrToken token)
	{
		if (token.offset == 0)
			return d_tree.get(i_pred);
		
		int cIndex = i_pred + token.offset;
		
		if (0 < cIndex && cIndex < d_tree.size())
			return d_tree.get(cIndex);
		
		return null;
	}
}
