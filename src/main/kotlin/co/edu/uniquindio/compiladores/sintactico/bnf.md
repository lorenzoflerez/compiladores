**BNF**


```ecma script level 4
<UnidadDeCompilacion> ::= class identificador begin [<BloqueImports>] <BloqueFunciones> end

<BloqueImports> ::= include llaveIzquierda <ListaImports> llaveDerecha 

<ListaImports> ::= <Import> [, <ListaImports>]

<Import> ::= identificador

<BloqueFunciones> ::= llaveIzquierda <ListaFunciones> llaveDerecha 

<ListaFunciones> ::= <Funcion> [<ListaFunciones>]

<Funcion> ::= function identificador ( [<ListaParametros>] ) [typeOf <Tipodato>] <BloqueSentencias> 

<ListaParametros> ::= <Parametro> [, <ListaParametros>]

<Parametro> ::= <Tipodato> identificador

<BloqueSentencias> ::= llaveIzquierda [<ListaSentencias>] llaveDerecha 

<ListaSentencias> ::= <Sentencia> [<ListaSentencias>]

<Sentencia> ::= <DeclaracionVariable> | <DeclaracionArreglo> | <Asignacion> | <Lectura> | <Impresion> | <InvocacionFuncion> | <Condicional> | <Ciclo> | <Seleccion> | <Retorno> | <Control> | <Incremento> | <Decremento>

<DeclaracionVariable> ::= <Variable> | <Constante>

<Variable> ::= var <Tipodato> identificador [operadorAsignacion <Expresion>];

<Constante> ::= const <Tipodato> identificador operadorAsignacion <Expresion>;  

<DeclaracionArreglo> ::= array <Tipodato> identificador operadorAsignacion corcheteIzquierdo <Expresion> corcheteDerecho;

<Asignacion> ::= identificador operadorAsignacion <Expresion> 

<Lectura> ::= read identificador;

<Impresion> ::= print <Expresion>

<InvocacionFuncion> ::= new identificador ([<ListaParametros>]); 

<Incremento> ::= operadorIncremento identificador

<Decremento> ::= operadorDecremento identificador

<Condicional> ::= if (<ExpresionLogica>) do <BloqueSentencias> [else <BloqueSentencias>]

<Ciclo> ::= <CicloFor> | <CicloWhile> | <CicloDoWhile>

<CicloFor> ::= for (<Variable> in range <ValorNumerico>) do <BloqueSentencias>

<CicloWhile> ::= while(<ExpresionLogica>) do <BloqueSentencias>

<CicloDoWhile> ::= do <BloqueSentencias> while(<ExpresionLogica>) 

<Seleccion> ::= switch (<Expresion>) do <BloqueOpciones>

<BloqueOpciones> ::= llaveIzquierda <ListaOpciones> llaveDerecha

<ListaOpciones> ::= <Opcion> [<ListaOpciones>]

<Opcion> ::= case <Valor> : <BloqueSentencias> break ;

<Retorno> ::= return <Expresion> ;

<Control> ::= try <BloqueSentencias> catch (<Tipodato> identificador) continue;

<Expresion> ::= <ExpresionCadena> | <ExpresionLogica> | <ExpresionRelacional> | <ExpresionAritmetica>

<ExpresionCadena> ::= cadena [+ <ExpresionCadena>] | identificador

<ExpresionLogica> ::= [ operadorNegacion ] [(] <ExpresionLogica> [)] | <ExpresionLogica> operadorAND <ExpresionLogica> | <ExpresionLogica> operadorOR <ExpresionLogica> | <ExpresionRelacional> | true | false

<ExpresionRelacional> ::= <ExpresionCadena> | <ExpresionAritmetica> | <ExpresionRelacional> operadorRelacional <ExpresionRelacional>

<ExpresionAritmetica> ::= <ExpresionAritmetica> operardorAritmetico <ExpresionAritmetica> | (<ExpresionAritmetica>) |<ValorNumerico> 

<Valor> ::= <ValorNumerico> | caracter | cadenaCaracteres

<ValorNumerico> ::= [<Signo>] decimal | [<Signo>] entero | [<Signo>] identificador

<Signo> ::= - | +

<Tipodato> ::= boolean | int |float | string | char | class identificador	

