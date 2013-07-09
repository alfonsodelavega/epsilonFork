/*
* generated by Xtext
*/
package org.eclipse.epsilon.eugenia.examples.executablestatemachine.textual.ui.contentassist.antlr;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;

import org.antlr.runtime.RecognitionException;
import org.eclipse.xtext.AbstractElement;
import org.eclipse.xtext.ui.editor.contentassist.antlr.AbstractContentAssistParser;
import org.eclipse.xtext.ui.editor.contentassist.antlr.FollowElement;
import org.eclipse.xtext.ui.editor.contentassist.antlr.internal.AbstractInternalContentAssistParser;

import com.google.inject.Inject;

import org.eclipse.epsilon.eugenia.examples.executablestatemachine.textual.services.ActionsGrammarAccess;

public class ActionsParser extends AbstractContentAssistParser {
	
	@Inject
	private ActionsGrammarAccess grammarAccess;
	
	private Map<AbstractElement, String> nameMappings;
	
	@Override
	protected org.eclipse.epsilon.eugenia.examples.executablestatemachine.textual.ui.contentassist.antlr.internal.InternalActionsParser createParser() {
		org.eclipse.epsilon.eugenia.examples.executablestatemachine.textual.ui.contentassist.antlr.internal.InternalActionsParser result = new org.eclipse.epsilon.eugenia.examples.executablestatemachine.textual.ui.contentassist.antlr.internal.InternalActionsParser(null);
		result.setGrammarAccess(grammarAccess);
		return result;
	}
	
	@Override
	protected String getRuleName(AbstractElement element) {
		if (nameMappings == null) {
			nameMappings = new HashMap<AbstractElement, String>() {
				private static final long serialVersionUID = 1L;
				{
					put(grammarAccess.getActionAccess().getAlternatives(), "rule__Action__Alternatives");
					put(grammarAccess.getSetAccess().getGroup(), "rule__Set__Group__0");
					put(grammarAccess.getIncAccess().getGroup(), "rule__Inc__Group__0");
					put(grammarAccess.getDecAccess().getGroup(), "rule__Dec__Group__0");
					put(grammarAccess.getIfAccess().getGroup(), "rule__If__Group__0");
					put(grammarAccess.getPrintAccess().getGroup(), "rule__Print__Group__0");
					put(grammarAccess.getSetAccess().getVarAssignment_1(), "rule__Set__VarAssignment_1");
					put(grammarAccess.getSetAccess().getValueAssignment_3(), "rule__Set__ValueAssignment_3");
					put(grammarAccess.getIncAccess().getVarAssignment_1(), "rule__Inc__VarAssignment_1");
					put(grammarAccess.getDecAccess().getVarAssignment_1(), "rule__Dec__VarAssignment_1");
					put(grammarAccess.getIfAccess().getVarAssignment_1(), "rule__If__VarAssignment_1");
					put(grammarAccess.getIfAccess().getValueAssignment_3(), "rule__If__ValueAssignment_3");
					put(grammarAccess.getPrintAccess().getMsgAssignment_2(), "rule__Print__MsgAssignment_2");
				}
			};
		}
		return nameMappings.get(element);
	}
	
	@Override
	protected Collection<FollowElement> getFollowElements(AbstractInternalContentAssistParser parser) {
		try {
			org.eclipse.epsilon.eugenia.examples.executablestatemachine.textual.ui.contentassist.antlr.internal.InternalActionsParser typedParser = (org.eclipse.epsilon.eugenia.examples.executablestatemachine.textual.ui.contentassist.antlr.internal.InternalActionsParser) parser;
			typedParser.entryRuleAction();
			return typedParser.getFollowElements();
		} catch(RecognitionException ex) {
			throw new RuntimeException(ex);
		}		
	}
	
	@Override
	protected String[] getInitialHiddenTokens() {
		return new String[] { "RULE_WS", "RULE_ML_COMMENT", "RULE_SL_COMMENT" };
	}
	
	public ActionsGrammarAccess getGrammarAccess() {
		return this.grammarAccess;
	}
	
	public void setGrammarAccess(ActionsGrammarAccess grammarAccess) {
		this.grammarAccess = grammarAccess;
	}
}