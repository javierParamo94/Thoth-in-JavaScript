package src.client.gui.utils;

import src.client.core.grammar.Grammar;
import src.client.gui.mainGui;
import src.client.gui.mediator.MediatorRenameSymbol;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * <b>Descripción</b><br>
 * Clase que contiene la parte visual del renombrado de símbolos por medio de
 * una ventana de diálogo.
 * <p>
 * <b>Detalles</b><br>
 * Crea el cuadro de texto con los diferentes elementos y llama al mediador para
 * que sustituya el nodo terminal o no terminal por su renombrado.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Al pulsar el botón de aceptar trata de realizar el renombrado y la ventana
 * desaparece. En caso de pulsar en cancelar la ventana desaparece directamente<br>
 * </p>
 * 
 * @author Francisco Javier Páramo Arnáiz
 * @version 1.0
 */
public class RenameSymbolDialog extends DialogBox {

	// Attributes
	// -----------------------------------------------------------------

	/**
	 * Recuadro donde se escribe el renombre.
	 */
	public TextBox mChoiceRename;
	/**
	 * Variable para la internacionalización de los textos
	 */
	private MessageMessages mMsg = GWT.create(MessageMessages.class);
	/**
	 * Mediador del renombrado de símbolos.
	 */
	private MediatorRenameSymbol mMediator;
	/**
	 * Botón cancelar para cerrar el recuadro de diálogo.
	 */
	private Button mCancel;
	/**
	 * Botón aceptar para realizar el renombrado.
	 */
	private Button mAccept;
	/**
	 * Nodos que aparecerán en el recuadro de sugerencias.
	 */
	public MultiWordSuggestOracle suggestionNodes = new MultiWordSuggestOracle();
	/**
	 * Recuador de sugerencias donde elegiremos el nodo a reemplazar.
	 */
	public SuggestBox mChoiceFind = new SuggestBox(suggestionNodes);

	// Methods
	// -----------------------------------------------------------------

	/**
	 * Constructor completo del DialogBox de renombrado de nodos.
	 * 
	 * @param visualMain
	 *            Viasual de la vista principal.
	 * @param grammar
	 *            Gramática que se le pasa.
	 */
	public RenameSymbolDialog(mainGui visualMain, Grammar grammar) {
		// mediador
		mMediator = new MediatorRenameSymbol(visualMain, this, grammar);
		// características del recuadrode diálogo.
		setText(mMsg.renamesymbol());
		setAnimationEnabled(true);
		setGlassEnabled(true);
		center();
		// Panel donde van colocados los elementos
		VerticalPanel searchPanel = new VerticalPanel();
		VerticalPanel generalPanel = new VerticalPanel();
		HorizontalPanel replacePanel = new HorizontalPanel();
		HorizontalPanel byPanel = new HorizontalPanel();
		HorizontalPanel buttonPanel = new HorizontalPanel();

		replacePanel.setSpacing(10);
		byPanel.setSpacing(10);
		buttonPanel.setSpacing(10);

		HTML replace = new HTML(mMsg.find());
		HTML by = new HTML(mMsg.by());

		mChoiceRename = new TextBox();

		replacePanel.add(replace);
		replacePanel.add(mChoiceFind);
		byPanel.add(by);
		byPanel.add(mChoiceRename);
		// Panel que engloba "sustituir" y "por".
		searchPanel.add(replacePanel);
		searchPanel.add(byPanel);
		// Panel de botones
		mCancel = new Button(mMsg.exit());
		mAccept = new Button(mMsg.accept());
		buttonPanel.add(mAccept);
		buttonPanel.add(mCancel);
		// Panel de botones + panel de búsqueda y renombrado
		generalPanel.add(searchPanel);
		generalPanel.add(buttonPanel);
		setWidget(generalPanel);

		buildListeners();
	}// RenameSymbolDialog

	/**
	 * Asigna la funcionalidad de los botones.
	 */
	public void buildListeners() {
		// Botón cancelar
		mCancel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				RenameSymbolDialog.this.hide();
			}
		});
		// Botón aceptar.
		mAccept.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				mMediator.accept();
				RenameSymbolDialog.this.hide();
			}
		});
	}// buildListeners
}// RenameSymbolDialog
