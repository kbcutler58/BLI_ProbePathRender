/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cameraImpl;

import com.jme3.input.InputManager;
import com.jme3.renderer.Camera;
import mouseKeyboard.GeneralKeyboardActionMethod;

/**
 *
 * @author BLI
 */
public class CameraMovement extends GeneralKeyboardActionMethod{
    
    private Camera camera;
    private String name;
    
    public CameraMovement(InputManager manager, String name, int keyCode, Camera camera){
        super(manager,name,keyCode);
        this.camera = camera;
        this.name = name;
    }

    @Override
    public void actionMethod() {
        System.out.println(name + " from Location of " + camera.getLocation().toString());
    }
    
}