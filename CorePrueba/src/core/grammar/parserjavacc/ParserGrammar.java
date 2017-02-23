/* Generated By:JavaCC: Do not edit this line. ParserGrammar.java */
package core.grammar.parserjavacc;

import java.io.StringReader;
import java.util.Vector;

import core.*;
import core.grammar.*;

/**
 * <b>Descripci�n</b><br>
 * Interpreta la gram�tica y la construye en la aplicaci�n.
 * <p>
 * <b>Detalles</b><br>
 * Se encarga de la interpretaci�n de la gram�tica teniendo en cuenta que
 * �sta puede tener terminales (representados en min�sculas) y los no
 * terminales (representados en may�sculas).<br>
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Interpretar la gram�tica.
 * </p>
 * 
 * @author �lvar Arn�iz Gonz�lez, Andr�s Arn�iz Moreno
 * @version 1.0
 */
public class ParserGrammar implements ParserGrammarConstants {

  /**
   * �nica instancia de la clase, posibilita el patr�n Singleton.
   */
  private static ParserGrammar mInstance;
  
  /**
   * Cuando es verdadero 
   */
  private static boolean mContainsAxiom;

  /**
   * Devuelve la �nica instancia de la clase actualiz�ndola con el StringReader
   * que se le ha pasado.
   * 
   * @param sr Nueva entrada para el parser.
   * @return Instancia del parser para la entrada dada.
   */
  public static ParserGrammar getInstance (StringReader sr) {
      if(mInstance == null)
          mInstance = new ParserGrammar(sr);
      
      ReInit(sr);
      return mInstance;
  }//getInstance
  
  /**
   * Construye una gram�tica y la devuelve.<br>
   * Antes de devolverla calcula su tipo.
   *
   * @return Devuelve la gram�tica constru�da.
   * @throws ParseException En caso de que se haya producido un error.
   */
  public Grammar buildGrammar () throws ParseException {
    Grammar grammar;

    mContainsAxiom = false;
    grammar = sent();

    if(!mContainsAxiom)
      throw new ParseException(ParseException.AXIOM_ERROR);

    grammar.calculateType();

    return grammar;
  }//buildGrammar

  /**
   * Comprueba que la parte izquierda de al menos una producci�n tenga el axioma.
   * 
   * @param v Vector de s�mbolos donde queremos saber si s�lo est� el axioma.
   */

  private static void isAxiom (NonTerminal axiom, Vector<Symbol> v){

    if(v.size() == 1 && axiom.equals(v.firstElement()))
      mContainsAxiom = true;

  }

/*********************************************************
*        ESPECIFICACI�N DE LA GRAM�TICA                  *
*********************************************************/
  static final public Grammar sent() throws ParseException {
  Grammar grammar = new Grammar();
  Vector<Symbol> left, right;
  Token axiom;
    jj_consume_token(SEP);
    jj_consume_token(12);
    axiom = jj_consume_token(NOTERM);
                                                grammar.setAxiom(new NonTerminal((String)axiom.image));
    jj_consume_token(SEP);
    jj_consume_token(SEP);
    label_1:
    while (true) {
      left = prodLeft();
      jj_consume_token(ARROW);
      right = prodRight();
                                                if(!mContainsAxiom)
                                                  isAxiom(grammar.getAxiom(), left);
                                                grammar.createProduction(left, right);
      label_2:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case SEL:
          ;
          break;
        default:
          jj_la1[0] = jj_gen;
          break label_2;
        }
        jj_consume_token(SEL);
        right = prodRight();
                                                grammar.createProduction(left, right);
      }
      jj_consume_token(END);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case TERM:
      case NOTERM:
        ;
        break;
      default:
        jj_la1[1] = jj_gen;
        break label_1;
      }
    }
    jj_consume_token(SEP);
    jj_consume_token(SEP);
  {if (true) return grammar;}
    throw new Error("Missing return statement in function");
  }

 //sent
  static final public Vector<Symbol> prodLeft() throws ParseException {
  Vector<Symbol> v = new Vector<Symbol>(5,5);
  Token t;
    label_3:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case TERM:
        ;
        break;
      default:
        jj_la1[2] = jj_gen;
        break label_3;
      }
      t = jj_consume_token(TERM);
                                        v.add(new Terminal((Character)t.image.toCharArray()[0]));
    }
    //Al menos un no terminal
      t = jj_consume_token(NOTERM);
                                        v.add(new  NonTerminal((String)t.image));
    label_4:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case TERM:
      case NOTERM:
        ;
        break;
      default:
        jj_la1[3] = jj_gen;
        break label_4;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case TERM:
        t = jj_consume_token(TERM);
                                        v.add(new Terminal((Character)t.image.toCharArray()[0]));
        break;
      case NOTERM:
        t = jj_consume_token(NOTERM);
                                        v.add(new  NonTerminal((String)t.image));
        break;
      default:
        jj_la1[4] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
    }
  {if (true) return v;}
    throw new Error("Missing return statement in function");
  }

 //prodLeft
  static final public Vector<Symbol> prodRight() throws ParseException {
  Vector<Symbol> v = new Vector<Symbol>(5,5);
  Token t;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case TERM:
    case NOTERM:
      label_5:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case TERM:
          t = jj_consume_token(TERM);
                                                v.add(new Terminal((Character)t.image.toCharArray()[0]));
          break;
        case NOTERM:
          t = jj_consume_token(NOTERM);
                                                v.add(new  NonTerminal((String)t.image));
          break;
        default:
          jj_la1[5] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case TERM:
        case NOTERM:
          ;
          break;
        default:
          jj_la1[6] = jj_gen;
          break label_5;
        }
      }
      break;
    case EPS:
      jj_consume_token(EPS);
                                                v = new Vector<Symbol>(1, 0);
                                                v.add(new TerminalEpsilon());
      break;
    default:
      jj_la1[7] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  {if (true) return v;}
    throw new Error("Missing return statement in function");
  }

  static private boolean jj_initialized_once = false;
  static public ParserGrammarTokenManager token_source;
  static SimpleCharStream jj_input_stream;
  static public Token token, jj_nt;
  static private int jj_ntk;
  static private int jj_gen;
  static final private int[] jj_la1 = new int[8];
  static private int[] jj_la1_0;
  static {
      jj_la1_0();
   }
   private static void jj_la1_0() {
      jj_la1_0 = new int[] {0x200,0x60,0x20,0x60,0x60,0x60,0x60,0x160,};
   }

  private ParserGrammar(java.io.InputStream stream) {
     this(stream, null);
  }
  private ParserGrammar(java.io.InputStream stream, String encoding) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser.  You must");
      System.out.println("       either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new ParserGrammarTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 8; i++) jj_la1[i] = -1;
  }

  static public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  static public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 8; i++) jj_la1[i] = -1;
  }

  private ParserGrammar(java.io.Reader stream) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser.  You must");
      System.out.println("       either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new ParserGrammarTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 8; i++) jj_la1[i] = -1;
  }

  static public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 8; i++) jj_la1[i] = -1;
  }

  private ParserGrammar(ParserGrammarTokenManager tm) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser.  You must");
      System.out.println("       either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 8; i++) jj_la1[i] = -1;
  }

  public void ReInit(ParserGrammarTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 8; i++) jj_la1[i] = -1;
  }

  static final private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }

  static final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

  static final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  static final private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  static private java.util.Vector jj_expentries = new java.util.Vector();
  static private int[] jj_expentry;
  static private int jj_kind = -1;

  static public ParseException generateParseException() {
    jj_expentries.removeAllElements();
    boolean[] la1tokens = new boolean[13];
    for (int i = 0; i < 13; i++) {
      la1tokens[i] = false;
    }
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 8; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 13; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.addElement(jj_expentry);
      }
    }
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = (int[])jj_expentries.elementAt(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  static final public void enable_tracing() {
  }

  static final public void disable_tracing() {
  }

   //isAxiom


}
