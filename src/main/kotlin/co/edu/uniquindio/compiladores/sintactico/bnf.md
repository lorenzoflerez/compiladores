**BNF**


```ecma script level 4
<UnidadDeCompilacion> ::= class identificador begin [<ListaImport>] <BloqueFunciones> end

<ListaImport> ::= include llaveIzquierda <Import> llaveDerecha

<Import> ::= identificador; [<Import>]

<BloqueFunciones> ::= llaveIzquierda <ListaFunciones> llaveDerecha 

<ListaFunciones> ::= <Funcion> [<ListaFunciones>]

<Funcion> ::= function identificador ( [<ListaParametros>] ) [typeOf <Tipodato>] <BloqueSentencias> 

<ListaParametros> ::= <Parametro> [, <ListaParametros>]

<Parametro> ::= <Tipodato> identificador

<BloqueSentencias> ::= llaveIzquierda [<ListaSentencias>] llaveDerecha 

<ListaSentencias> ::= <Sentencia> [<ListaSentencias>]

<Sentencia> ::= <DeclaracionVariable> | <DeclaracionArreglo> | <Asignacion> | <Lectura> | <Impresion> | <InvocacionFuncion> | <Decision> | <Ciclo> | <Seleccion> | <Retorno> | <Control> | <Incremento> | <Decremento>

<DeclaracionVariable> ::= <DeclaracionVariable> | <Variables> | <Constantes>

<Variables> ::= <Variables> | <Variable>

<Variable> ::= var <Tipodato> identificador [operadorAsignacion <Expresion>];

<Constantes> ::= <Constantes> | <Constante>

<Constante> ::= const <Tipodato> identificador =: <Expresion>;  

<DeclaracionArreglo> ::= array <Tipodato> identificador =: corcheteIzquierdo <ListaExpresiones> corcheteDerecho;

<Asignacion> ::= identificador operadorAsignacion <Expresion> |  identificador =: corcheteIzquierdo <ListaExpresiones> corcheteDerecho;

<Lectura> ::= read identificador ;

<Impresion> ::= print : <ListaExpresiones>

<InvocacionFuncion> ::= new identificador (<ListaParametros>); 

<Incremento> ::= identificador operadorIncremento | operadorIncremento identificador

<Decremento> ::= identificador operadorDecremento | operadorDecremento identificador

<Condicional> ::= if (<ExpresionLogica>) do <BloqueSentencias> [<BloqueAlternativa>] [else <BloqueSentencias>]

<BloqueAlternativa> ::= <Alternativa> [<BloqueAlternativa>]

<Alternativa> ::= else if (<ExpresionLogica>) do <BloqueSentencias>

<Ciclo> ::= <CicloFor> | <CicloWhile> | <CicloDoWhile>

<CicloFor> ::= for (<Variable> in range <ValorNumerico>) do <BloqueSentencias>

<CicloWhile> ::= while(<ExpresionLogica>) do <BloqueSentencias>

<CicloDoWhile> ::= do <BloqueSentencias> while(<ExpresionLogica>) 

<Seleccion> ::= switch (<Expresion>) do <BloqueOpciones>

<BloqueOpciones> ::= llaveIzquierda <Opciones> llaveDerecha

<Opciones> ::= <Opciones> <Opcion> [default: <BloqueSentencias> break ;] | <Opcion> [default: <BloqueSentencias> break ;]

<Opcion> ::= case <Valor> : <BloqueSentencias> break ;

<Retorno> ::= return <Expresion> ;

<Control> ::= try <BloqueSentencias> catch (<Tipodato> identificador) continue;

<ListaExpresiones> ::= [<ListaExpresiones>, ] <Expresion>;

<Expresion> ::= <ExpresionCadena> | <ExpresionLogica> | <ExpresionRelacional> | <ExpresionAritmetica> | <InvocacionFuncion>

<ExpresionCadena> ::= cadena [+ <ExpresionCadena>] | identificador

<ExpresionLogica> ::= [operadorNegacion] <ExpresionLogica> | (<ExpresionLogica>) | <ExpresionLogica> operadorAND <ExpresionLogica> | <ExpresionLogica> operadorOR <ExpresionLogica> | <ExpresionRelacional> | true | false

<ExpresionRelacional> ::= <ExpresionCadena> | <ExpresionAritmetica>	|  <ExpresionRelacional>  operadorRelacional <ExpresionRelacional>

<ExpresionAritmetica> ::= <ExpresionAritmetica> operardorAritmetico <ExpresionAritmetica> | (<ExpresionAritmetica>) |<ValorNumerico> 

<Valor> ::= <ValorNumerico> | caracter | cadenaCaracteres

<ValorNumerico> ::= [<Signo>] decimal | [<Signo>] entero | [<Signo>] identificador

<Signo> ::= - | +

<Tipodato> ::= boolean | int |float | string | char | class identificador	

