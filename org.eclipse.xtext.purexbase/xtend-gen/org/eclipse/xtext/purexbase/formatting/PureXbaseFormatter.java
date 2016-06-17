/**
 * generated by Xtext
 */
package org.eclipse.xtext.purexbase.formatting;

import com.google.inject.Inject;
import org.eclipse.xtext.Assignment;
import org.eclipse.xtext.Keyword;
import org.eclipse.xtext.TerminalRule;
import org.eclipse.xtext.formatting.impl.AbstractDeclarativeFormatter;
import org.eclipse.xtext.formatting.impl.FormattingConfig;
import org.eclipse.xtext.purexbase.services.PureXbaseGrammarAccess;
import org.eclipse.xtext.xbase.formatting.XbaseFormatter;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.services.XbaseGrammarAccess;
import org.eclipse.xtext.xbase.services.XtypeGrammarAccess;

/**
 * This class contains custom formatting description.
 * 
 * See https://www.eclipse.org/Xtext/documentation/303_runtime_concepts.html#formatting
 * on how and when to use it.
 */
@SuppressWarnings("all")
public class PureXbaseFormatter extends AbstractDeclarativeFormatter {
  @Inject
  @Extension
  private PureXbaseGrammarAccess _pureXbaseGrammarAccess;
  
  @Inject
  private XbaseFormatter xbaseFormatter;
  
  @Override
  protected void configureFormatting(final FormattingConfig c) {
    XbaseGrammarAccess _xbaseGrammarAccess = this._pureXbaseGrammarAccess.getXbaseGrammarAccess();
    this.xbaseFormatter.configure(c, _xbaseGrammarAccess);
    final PureXbaseGrammarAccess.SpecialBlockExpressionElements sbee = this._pureXbaseGrammarAccess.getSpecialBlockExpressionAccess();
    FormattingConfig.LinewrapLocator _setLinewrap = c.setLinewrap(1, 2, 3);
    Assignment _expressionsAssignment_1_0 = sbee.getExpressionsAssignment_1_0();
    _setLinewrap.after(_expressionsAssignment_1_0);
    FormattingConfig.LinewrapLocator _setLinewrap_1 = c.setLinewrap(1, 2, 3);
    Keyword _semicolonKeyword_1_1 = sbee.getSemicolonKeyword_1_1();
    _setLinewrap_1.after(_semicolonKeyword_1_1);
    FormattingConfig.NoSpaceLocator _setNoSpace = c.setNoSpace();
    Keyword _semicolonKeyword_1_1_1 = sbee.getSemicolonKeyword_1_1();
    _setNoSpace.before(_semicolonKeyword_1_1_1);
    FormattingConfig.LinewrapLocator _setLinewrap_2 = c.setLinewrap(1, 2, 3);
    XtypeGrammarAccess.XImportSectionElements _xImportSectionAccess = this._pureXbaseGrammarAccess.getXImportSectionAccess();
    Assignment _importDeclarationsAssignment = _xImportSectionAccess.getImportDeclarationsAssignment();
    _setLinewrap_2.after(_importDeclarationsAssignment);
    FormattingConfig.LinewrapLocator _setLinewrap_3 = c.setLinewrap(0, 1, 3);
    TerminalRule _sL_COMMENTRule = this._pureXbaseGrammarAccess.getSL_COMMENTRule();
    _setLinewrap_3.before(_sL_COMMENTRule);
    FormattingConfig.LinewrapLocator _setLinewrap_4 = c.setLinewrap(0, 1, 3);
    TerminalRule _mL_COMMENTRule = this._pureXbaseGrammarAccess.getML_COMMENTRule();
    _setLinewrap_4.before(_mL_COMMENTRule);
    FormattingConfig.LinewrapLocator _setLinewrap_5 = c.setLinewrap(0, 1, 3);
    TerminalRule _mL_COMMENTRule_1 = this._pureXbaseGrammarAccess.getML_COMMENTRule();
    _setLinewrap_5.after(_mL_COMMENTRule_1);
  }
}