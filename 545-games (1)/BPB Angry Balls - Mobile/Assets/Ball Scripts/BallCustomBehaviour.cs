using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.InputSystem;

public class BallCustomBehaviour : MonoBehaviour {
    // Note that when you add something with Serializedfield
    // that will be available in inspector view. So we can 
    // set value/ reference to these fields from inspector view.

    // Get delay to respawn the ball
    [SerializeField] private float ballRespawnDelay;
    // The time delay which may take to hit the target,
    // so we can remove the ball
    [SerializeField] private float ballRemoveDelay;
    // Get the Ball Prefab
    [SerializeField] private GameObject ballPrefab;
    // Get the Ball Pin
    [SerializeField] private Rigidbody2D ballPin;
    
    // As the Ball is removed from scene, ballRigidbody and 
    // ballSpringJoint has no meaning to be added as 
    // SerializeField.
    private Rigidbody2D ballRigidbody;
    private SpringJoint2D ballSpringJoint;
    private Camera camera;
    private bool isDragging;

    // Start is called before the first frame update
    void Start() {
        camera = Camera.main;

        // Make the first ball available
        respawnTheBall();
    }

    // Update is called once per frame
    void Update() {
        if (ballRigidbody == null) return;

        if (Touchscreen.current.primaryTouch.press.isPressed) {
            // When user is touching (and moving on) the screen.
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
        Vector2 touchPosition = 
            Touchscreen.current.primaryTouch.position.ReadValue();
        
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

        // Respawn the ball after delay
        Invoke(nameof(respawnTheBall), ballRespawnDelay);
    }

    private void respawnTheBall() {
        // Create an instance of the Ball prefab, set it to 
        // the same position of Ball pin and set the default 
        // rotation as rotation does not matter as of now.
        GameObject ball = Instantiate(
            ballPrefab, 
            ballPin.position, 
            Quaternion.identity);
    
        // Set Rigidbody and SpringJoint 
        ballRigidbody = ball.GetComponent<Rigidbody2D>();
        ballSpringJoint = ball.GetComponent<SpringJoint2D>();
        // Attach the ball to ball-pin
        ballSpringJoint.connectedBody = ballPin;
    }
}


public class HelloUnity : MonoBehaviour
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