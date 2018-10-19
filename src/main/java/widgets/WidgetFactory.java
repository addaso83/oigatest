package widgets;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class WidgetFactory {
	private  List<wigets> lstWidgetsFabricados=new ArrayList<wigets>();
	private  final long TiempoTope = 60000;
	private  long Tiempo = System.currentTimeMillis();
	private  int widgetsCreados = 1;
	private  int widgetsEnCola = 0;
	private  int TotalWidgetsFabricados = 0;
	private  int TotalEnviados = 0;
	private int disponibles = 0;
    private int restarAWidgetEnCola = 0;

	public  void setFabricarWidgets() {
		    if( (System.currentTimeMillis() - Tiempo) <= TiempoTope) {
		    	while(widgetsCreados <= 5) {
		                if(lstWidgetsFabricados.size()==0){
		    			wigets w= new wigets();
		    			w.ID = 1;
		    			w.Name = "Widget " + 1;
		    			w.dispached = false;
		    			lstWidgetsFabricados.add(w);
		    		}
					else {
						wigets w= new wigets();
						wigets w1= new wigets();
						w1 = lstWidgetsFabricados.get(lstWidgetsFabricados.size()-1);
		    			w.ID = w1.ID + 1;
		    			w.Name = "Widget " + w.ID;
		    			w.dispached = false;
		    			lstWidgetsFabricados.add(w);
					}
		    		widgetsCreados++;
		    		TotalWidgetsFabricados++;
		    	}
		    }
		    else {
		    	Tiempo = System.currentTimeMillis();
		    	widgetsCreados = 0;
		    }
		    
		    System.out.println("TotalWidgetsFabricados " + TotalWidgetsFabricados);
	}

    public void setSolicitud(int cantidad) {
    	TotalEnviados += cantidad;
        setFabricarWidgets();
          
        for(wigets w1:lstWidgetsFabricados) {
        	if(w1.dispached) {
        		disponibles++;
        	}
        }
        
        if(widgetsEnCola <= disponibles) {
          	int i =0;
    		while(i <= widgetsEnCola) {
    			 for(wigets w : lstWidgetsFabricados) {
                 		if(w.dispached==false) {
                 			lstWidgetsFabricados.get(lstWidgetsFabricados.indexOf(w)).dispached = true;
                 			System.out.println("WIDGET DESPACHADO ID: " + w.ID + " NAME:" + w.Name  );
                 			restarAWidgetEnCola++;
                 		}        	 
                    }
    			i++;
    		}
          }
        widgetsEnCola -= restarAWidgetEnCola;
        restarAWidgetEnCola = 0;
     
        if(widgetsEnCola > disponibles) {
              	int totalWidgetsAplicados = 0;
              	for(wigets w : lstWidgetsFabricados) {
              		if(w.dispached==false) {
              			lstWidgetsFabricados.get(lstWidgetsFabricados.indexOf(w)).dispached = true;
              			System.out.println("WIDGET DESPACHADO ID: " + w.ID + " NAME:" + w.Name  );
              			totalWidgetsAplicados++;
              		}
              	 widgetsEnCola -= totalWidgetsAplicados;
              }

        }
        
        //Actualizo lista de disponible
        for(wigets w1:lstWidgetsFabricados) {
        	if(w1.dispached) {
        		disponibles++;
        	}
        }
        
        
        if(cantidad <= disponibles) {
    		int i =0;
    		while(i <= cantidad) {
    			 for(wigets w : lstWidgetsFabricados) {
                 		if(w.dispached==false) {
                 			lstWidgetsFabricados.get(lstWidgetsFabricados.indexOf(w)).dispached = true;
                 			System.out.println("WIDGET DESPACHADO ID: " + w.ID + " NAME:" + w.Name  );
                 		}        	 
                    }
    			i++;
    		}
        }
        
        
        if(cantidad > disponibles) {
        	int totalWidgetsAplicados = 0;
        	   for(wigets w : lstWidgetsFabricados) {
        		if(w.dispached==false) {
        			lstWidgetsFabricados.get(lstWidgetsFabricados.indexOf(w)).dispached = true;
        			System.out.println("WIDGET DESPACHADO ID: " + w.ID + " NAME:" + w.Name  );
        			totalWidgetsAplicados++;
        		}
        	     widgetsEnCola += cantidad - totalWidgetsAplicados;
        	 
               }	
         
    }
   cantidad = 0;
    }

    public Iterator<wigets> getWidgetsFactory(){
		return lstWidgetsFabricados.stream().filter( s -> s.dispached).iterator();
	}
}
