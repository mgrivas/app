package com.esei.mgrivas.polenalert;

import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.widget.Button;

import com.esei.mgrivas.polenalert.controllers.MainController;

/**
 * Created by Mark on 19/06/2015.
 */
public class MainControllerTest extends ActivityInstrumentationTestCase2<MainController> {
    public MainControllerTest() {
        super(MainController.class);
    }

    public void testActivityExists() {
        MainController activity = getActivity();
        assertNotNull(activity);
    }

    public void testButtons() {
        MainController activity = getActivity();
        final Button button = (Button) activity.findViewById(R.id.button_new_track);
        final Button button2 = (Button) activity.findViewById(R.id.button_list_track);
        final Button button3 = (Button) activity.findViewById(R.id.button_observer);
        final Button button4 = (Button) activity.findViewById(R.id.button_show_map);
        assertEquals("Nuevo Recorrido", button.getText());
        assertEquals("Listar Recorridos", button2.getText());
        assertEquals("Modo Observador", button3.getText());
        assertEquals("Mostrar Mapa de Polen", button4.getText());
        TouchUtils.clickView(this, button);
    }


}
