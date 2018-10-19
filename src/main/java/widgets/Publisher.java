/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package widgets;

import org.apache.qpid.amqp_1_0.jms.impl.*;
import javax.jms.*;
import java.io.*;
import java.util.Iterator;

class Publisher {

    public static void main(String []args) throws Exception {
        Listener listener=new Listener();
        String usuario = env("ACTIVEMQ_USER", "admin");
        String clave = env("ACTIVEMQ_PASSWORD", "password");
        String host = env("ACTIVEMQ_HOST", "localhost");
        int puerto = Integer.parseInt(env("ACTIVEMQ_PORT", "5672"));
        String destino = arg(args, 0, "topic://event");

      
        int size = 256;
        int terminar = 0;
        int cantidad = 0;

        String DATA = "abcdefghijklmnopqrstuvwxyz";
        String body = "";
        for( int i=0; i < size; i ++) {
            body += DATA.charAt(i%DATA.length());
        }

        ConnectionFactoryImpl factory = new ConnectionFactoryImpl(host, puerto, usuario, clave);
        Destination dest = null;
        if( destino.startsWith("topic://") ) {
            dest = new TopicImpl(destino);
        } else {
            dest = new QueueImpl(destino);
        }

        Connection connection = factory.createConnection(usuario, clave);
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageProducer producer = session.createProducer(dest);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

      
        int count = 1;
        while(true) {
        	InputStreamReader inp=new InputStreamReader(System.in);
        	BufferedReader read=new BufferedReader(inp);
        	System.out.println("Ingrese el # de Widgets Solicitados");
        	cantidad = Integer.parseInt(read.readLine());
        	TextMessage msg = session.createTextMessage("#:"+count);
        	msg.setIntProperty("widgets", cantidad);
        	producer.send(msg);
        	Thread.sleep(1000);
        	System.out.println("Para Salir Ingrese 1");
        	terminar = Integer.parseInt(read.readLine());
        	if(terminar==1) {
        		break;
        	}
        	 Iterator<wigets> listaWidgets= listener.getWidgetsFactory();
        	 while(listaWidgets.hasNext()) {
        		 wigets w = (wigets) listaWidgets.next();
        		 System.out.println("WIDGETS SOLICITADOS ID:" + w.ID + " Nombre:" + w.Name);
        	 }
        }

        producer.send(session.createTextMessage("SHUTDOWN"));
        Thread.sleep(1000*3);
        connection.close();
        System.exit(0);
    }

    private static String env(String key, String defaultValue) {
        String rc = System.getenv(key);
        if( rc== null )
            return defaultValue;
        return rc;
    }

    private static String arg(String []args, int index, String defaultValue) {
        if( index < args.length )
            return args[index];
        else
            return defaultValue;
    }

}