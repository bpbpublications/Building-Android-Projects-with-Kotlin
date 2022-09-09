using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.InputSystem;

public class BallCustomBehaviour : MonoBehaviour {
    [SerializeField] private float ballRemoveDelay;
    // Get the Ball Prefab
    [SerializeField] private GameObject ballPrefab;
    // Get the Ball Pin
    [SerializeField] private Rigidbody2D ballPin;
    // Get delay to respawn the ball
    [SerializeField] private float ballRespawnDelay;
    
    // As the Ball is removed from scene, ballRigidbody and 
    // ballSpringJoint has no meaning to be added as 
    // SerializeField.
    private Rigidbody2D ballRigidbody;
    private SpringJoint2D ballSpringJoint;
    private Camera camera;
    private bool isDragging;

    void Start() {
        camera = Camera.main;
        // Make the first ball available
        respawnTheBall();
    }

    void Update() {
        if (ballRigidbody == null) return;

        if (Touchscreen.current.primaryTouch.press.isPressed) {
            // If user is touching the screen, and might be moving on screen.
            dragBall();
        } else {
            // User not touching the screen now
            if (isDragging) {
                releaseBall();
            }

            isDragging = false;
        }
    }

    private void dragBall() {
	    isDragging = true;
	    ballRigidbody.isKinematic = true;
	    // Reading the touch position, in terms of Screen Space
	    Vector2 touchPosition = Touchscreen.current.primaryTouch.position.ReadValue();
	    
	    // Coverting touch position to World Space position
	    Vector3 worldPosition = camera.ScreenToWorldPoint(touchPosition);
	    
	    // Set ball position to current world position
	    ballRigidbody.position = worldPosition;
	}

    private void releaseBall() {
        ballRigidbody.isKinematic = false;
        ballRigidbody = null;

        Invoke(nameof(removeTheBall), ballRemoveDelay);
    }

    private void removeTheBall() {
        ballSpringJoint.enabled = false;
        ballSpringJoint = null;

        // Respawn the ball
        Invoke(nameof(respawnTheBall), ballRespawnDelay);
    }

    private void respawnTheBall() {
        // Create an instance of the Ball prefab, set it to 
        // the same position of Ball pin and set the default 
        // rotation as rotation does not matter as of now.
        GameObject ball = Instantiate(ballPrefab, ballPin.position, Quaternion.identity);
    
        // Set Rigidbody and SpringJoint 
        ballRigidbody = ball.GetComponent<Rigidbody2D>();
        ballSpringJoint = ball.GetComponent<SpringJoint2D>();
        // Attach the ball to ball-pin
        ballSpringJoint.connectedBody = ballPin;
    }
}

/*
Step1 : Initial introduction to script

public class BallCustomBehaviour : MonoBehaviour
{
    // Start is called before the first frame update
    void Start()
    {
        
    }

    // Update is called once per frame
    void Update()
    {
    
    }
}
*/

/*
Step2 : While explaining script and touches

public class BallCustomBehaviour : MonoBehaviour
{
    // Start is called before the first frame update
    void Start()
    {
        
    }

    // Update is called once per frame
    void Update()
    {
        if (Touchscreen.current.primaryTouch.press.isPressed) {
            Vector2 touchPosition = Touchscreen.current.primaryTouch.position.ReadValue();
            Debug.Log(touchPosition);
        }
    }
}
*/


/*
Step3 : Converting touches on screen space to world space

public class BallCustomBehaviour : MonoBehaviour
{
    private Camera camera;

    // Start is called before the first frame update
    void Start()
    {
        camera = Camera.main;
    }

    // Update is called once per frame
    void Update()
    {
        if (Touchscreen.current.primaryTouch.press.isPressed) {
            // Reading the touch position, in terms of Screen Space
            Vector2 touchPosition = Touchscreen.current.primaryTouch.position.ReadValue();
            
            // Coverting touch position to position in terms of World Space
            Vector3 worldPosition = camera.ScreenToWorldPoint(touchPosition);
            
            Debug.Log("touchPosition >> " + touchPosition + " worldPosition >>" + worldPosition);
        }
    }
}

touchPosition >> (24.1, 1035.3) worldPosition >>(-10.3, 4.6, -10.0)
UnityEngine.Debug:Log (object)
BallCustomBehaviour:Update () (at Assets/Game scripts/BallCustomBehaviour.cs:26)

.....

touchPosition >> (2265.3, 1041.2) worldPosition >>(10.4, 4.6, -10.0)
UnityEngine.Debug:Log (object)
BallCustomBehaviour:Update () (at Assets/Game scripts/BallCustomBehaviour.cs:26)

*/

/*
Step4 : Converting touches on screen space to world space

public class BallCustomBehaviour : MonoBehaviour
{
    [SerializeField] private Rigidbody2D ballRigidbody;
    private Camera camera;

    // Start is called before the first frame update
    void Start()
    {
        camera = Camera.main;
    }

    // Update is called once per frame
    void Update()
    {
        if (Touchscreen.current.primaryTouch.press.isPressed) {
            // Reading the touch position, in terms of Screen Space
            Vector2 touchPosition = Touchscreen.current.primaryTouch.position.ReadValue();
            
            // Coverting touch position to position in terms of World Space
            Vector3 worldPosition = camera.ScreenToWorldPoint(touchPosition);
            
            // Set ball position to current world position
            ballRigidbody.position = worldPosition;
        }
    }
}

*/


/*
Step5 : Working with single ball

public class BallCustomBehaviour : MonoBehaviour
{
    [SerializeField] private Rigidbody2D ballRigidbody; // NOte that when yo add something with Serializedfield, that will be available in inspector view.
    [SerializeField] private SpringJoint2D ballSpringJoint; // NOte that when yo add something with Serializedfield, that will be available in inspector view.
    [SerializeField] private float ballRemoveDelay; // NOte that when yo add something with Serializedfield, that will be available in inspector view.
    

    private Camera camera;
    private bool isDragging;

    // Start is called before the first frame update
    void Start()
    {
        camera = Camera.main;
    }

    // Update is called once per frame
    void Update()
    {
        if (ballRigidbody == null) return;

        if (Touchscreen.current.primaryTouch.press.isPressed) {
            // If user is touching the screen, and might be moving on screen.

            isDragging = true;
            ballRigidbody.isKinematic = true;
            // Reading the touch position, in terms of Screen Space
            Vector2 touchPosition = Touchscreen.current.primaryTouch.position.ReadValue();
            
            // Coverting touch position to position in terms of World Space
            Vector3 worldPosition = camera.ScreenToWorldPoint(touchPosition);
            
            // Set ball position to current world position
            ballRigidbody.position = worldPosition;
        } else {
            // User not touching the screen now

            if (isDragging) {
                releaseBall();
            }

            isDragging = false;
        }
    }

    private void releaseBall() {
        ballRigidbody.isKinematic = false;
        ballRigidbody = null;

        Invoke(nameof(removeTheBall), ballRemoveDelay);
    }

    private void removeTheBall() {
        ballSpringJoint.enabled = false;
        ballSpringJoint = null;
    }
}
*/