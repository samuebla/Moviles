Sam Blázquez Martín
Miguel Hernández García
Juan Diego Mendoza Reyes

Nos gustaría que la práctica sea re-evaluada al 5 en caso de que esté bien. Ya solucionamos el Requisito Mínimo de la práctica de mantener el aspect ratio y hemos corregido el resto de fallos mencionados en el comentario de retroalimentacion
de la práctica 1.

Cambios realizados Practica 1

-Eliminados métodos de dibujado en el Engine, getWidth(), getHeight(), updateSurfaceSize(). Eso deberia de ser cosa de graphics, sino hay código duplicado y no tiene ningún sentido.

-Cambiadas las escenas para que hagan un engine.getGraphics() cada vez que quieran dibujar algo o coger el ancho y el alto. El usuario no tendria que estar preocupandose de tener en cuenta el alto y ancho de la pantalla.
Eso deberia de hacerlo el Graphics y el Input

-Eliminado AssetContext y setBaseContext de Main Activity. El surface dispone ya el contexto de la actividad, no hace falta guardarla de nuevo.

-Eliminados métodos setScenemngr(), setScene() y popScene() de Engine y añadido método getSceneMngr() para que la lógica acceda desde ahi al SceneMngr. Ya que el sceneMngr es el que tiene el stack de escenas y es parte del engine
no tiene sentido que desde la escena no se pueda acceder directamente al SceneMngr si tiene una referencia al Engine.

-Creado SceneMngr en el Engine. El SceneMngr es una parte esencial del Engine y no tiene sentido que el usuario tenga que andar creandolo.

-Cambiados accesos al sceneMngr de la logica para que utilicen la nueva implementación. Adaptación a lo que hemos comentado anteriormente.

-Cambiados metodos handleInput() y render() de las escenas para que reciban los parametros que necesitan del Engine y elimanada referencia al Engine desde la escena.La escena no necesita guardar todo el Engine en una referencia por cada escena
Ahora en el render le pasas exclusivamente el Render (Graphics) y en el HandlInput se le pasa distintas partes del Engine que el usuario puede necesitar para que pueda interactuar con elementos de Audio, cambio de escenas etc.

-Añadido método loadResources a la escena. Solo una escena puede cargar recursos en todo el juego, esa escena debe de ser añadida al inicio de la ejecucion al engine con un setResourceScene. Esto es debido a que hay que esperar a la
 inicialización de los distintos modulos antes de poder hacer la carga de ficheros. 

-Eliminado inputReceive() de las escenas y movido al propio Input. Teníamos un método llamado InputRecieved con código duplicado por cada escena creada que no tenía ningún sentido. Ahora este inputRecieved lo realiza el propio Input.

-Eliminada llamada a handleInput() desde el update() de las escenas, añadida llamada a handleInput() del sceneManager desde el Engine después del update(). El sceneManager se encarga de llamar a la escena para que haga su handleInput(). No
tiene sentido que el usuario tenga que llamar a su handl input, el Engine deberia de ser el encargado de decidir cuando se llama.

• Respecto a la lógica:
-Cambiado escalado de las escenas, ahora la lógica tiene un tamaño lógico de 400 x 600 (Relacion 4/6), el IGraphics es el encargado en cada una de sus implementaciones de escalarlo con el tamaño de la ventana. El input convierte las posiciones logicas en posiciones de pantalla con la ayuda del IGraphics.
Ahora el usuario debería ser capaz de utilizar unas coordenadas bases relativas sin tener que preocuparse de la resolución de la ventana/pantalla. En nuestro caso es una escala de 0 a 1000 tanto de alto como de ancho. De esta manera eliminamos
tambien las referencias inútiles al GetWidth y GetHeight del engine que teniamos anteriormente, para que el usuario solo se preocupe de hacer las proporciones en funcion de esa misma escala.

-Mientras dura el comprobar si pulsas en casillas incorrectas éstas aparecen en rojo: Pensabamos que era así como se tenía que implementar, pero ahora si pulsas el botón de comprobar solo se revelaran las casillas que tuvieras seleccionadas anteriormente.

-El tablero no ocupa siempre el máximo de tamaño posible: Gracias al escalado mencionado anteriormente hemos cambiado las proporciones del tablero para que el tamaño de la celda y del texto se adapte ocupando lo máximo posible (Dejando de manera
intencional algo de margen en los laterales)

-Muy Grave: En Android las escenas se deforman. En móviles pequeños la información sale de pantalla: Lo mismo, con el nuevo escalado de 400x600 conseguimos que el tamaño de las cosas de las escenas se adaptasen. Se nos olvidó en la 
entrega anterior testear con móviles muy pequeños y muy grandes y no hicimos unos tamaños relativos al tamaño de la pantalla.


Opcional:
-Niveles no cuadrados, pero no se centra ocupando el máximo tamaño: Ahora mantenemos una relacion donde dividimos la pantalla en Rows+2 x Cols+2 (+2 Para posicionar el texto y tener algo de margen). De esta manera se aprovechha toda la pantalla y ocupan el máximo posible.
