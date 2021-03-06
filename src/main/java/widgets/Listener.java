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
import java.util.List;



public class Listener {	
    public static void main(String []args) throws JMSException {
    	WidgetFactory widget= new WidgetFactory();
        String usuario = env("ACTIVEMQUSER", "admin");
        String clave = env("ACTIVEMQ_PASSWORD", "password");
        String host = env("ACTIVEMQ_HOST", "localhost");
        int puerto = Integer.parseInt(env("ACTIVEMQ_PORT", "5672"));
        String destino = arg(args, 0, "topic://event"); 
        ConnectionFactoryImpl fabrica = new ConnectionFactoryImpl(host, puerto, usuario, clave);
        Destination dest = null;
        int cantidad = 0;
        
        if( destino.startsWith("topic://") ) {
            dest = new TopicImpl(destino);
        } else {
            dest = new QueueImpl(destino);
        }
        Connection conexion = fabrica.createConnection(usuario, clave);
        conexion.start();
        Session sesion = conexion.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageConsumer consumer = sesion.createConsumer(dest);
        System.out.println("Esperando Cantidad de Widgets");
        while(true) {
            Message msg = consumer.receive();
            if( msg instanceof  TextMessage ) {
                String body = ((TextMessage) msg).getText();
                if( "SHUTDOWN".equals(body)) {
                    System.out.println("SHUTDOWN");
                    conexion.close();
                    System.exit(1);
                } 
                else {
                    cantidad =	msg.getIntProperty("widgets");
                    widget.setFabricarWidgets();
                    widget.setSolicitud(cantidad);
                    }
                }
                else {
                System.out.println("Unexpected message type: "+msg.getClass());
            }
            
        }
           
        
    }
    
    
	public List<wigets> getWidgetsFactory(){
		WidgetFactory widget= new WidgetFactory();
		return widget.getWidgetsFactory();
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