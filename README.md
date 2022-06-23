# PoC Threads

Este es un proyecto de prueba para analizar como crear tareas asíncronas en Springboot.

Tiene dos modos de ejecución, controlados por la propiedad app.pool.enabled


## Creación de hilos bajo demanda (app.pool.enabled=false)

Con este modo, cada vez que se necesiten se van creado hilos (hasta llegar a un límite de 'app.async.maxThreads'. 
Existe un listener que cada 5 segundos va leyendo tareas disponibles de una tabla de BBDD, si detecta que hay tareas para procesar, calcula los hilos necesarios hasta llegar al límite y dispara la creación de estos hilos.

Las clases implicadas son:

* AsyncConfig -> Configuración de threads
* QueueJobEntity y QueueJobRepository -> Consulta en BBDD las tareas disponibles
* QueueJobServiceImpl -> Listener que consulta tareas de la tabla de BBDD y dispara los hilos
* ConsumerService -> Hilo asíncrono que ejecuta un algoritmo, en este caso llama a Orquestador que hace un bloqueo de la tarea de BBDD mediante un semáforo


## Pool de hilos creados y escuchando (app.pool.enabled=true)

Con este modo, al arrancar la aplicación se crea un pool de 'app.async.maxThreads' hilos que están escuchando una cola de ejecución.
Existe un listener que cada 5 segundos va leyendo tareas disponibles de una tabla de BBDD, si detecta que hay tarea para procesar, las vuelca a una cola de ejecución en memoria y despierta a los hilos que estén dormidos. Cada hilo coge un elemento de la cola (de manera sincronizada) y lo ejecuta. Si no quedan elementos en la cola, el hilo se queda dormido a la espera de un aviso.

Las clases implicadas son:

* AsyncConfig -> Configuración de threads
* QueueJobEntity y QueueJobRepository -> Consulta en BBDD las tareas disponibles
* PoolQueueJobServiceImpl -> Listener que consulta tareas de la tabla de BBDD y rellena la cola de ejecución
* ConsumerPoolService -> Hilo asíncrono que ejecuta intenta coger un elemento de la cola de ejecución, si no existe se duerme y si existe lo ejecuta llamando a Orquestador que hace un bloqueo de la tarea de BBDD mediante un semáforo
