package com.example.ar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.HitTestResult;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    private ArFragment arFragment;
    private ModelRenderable treeRenderable;
    private ModelRenderable treeRenderable2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.sceneform_fragment);

        ModelRenderable.builder()
                .setSource(this, R.raw.toon_pine)
                .build()
                .thenAccept(renderable -> treeRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load tree renderable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });
        ModelRenderable.builder()
                .setSource(this, R.raw.tree)
                .build()
                .thenAccept(renderable -> treeRenderable2 = renderable)
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load tree2 renderable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });
        arFragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                    if (treeRenderable == null) {
                        return;
                    }

                    Anchor anchor = hitResult.createAnchor();
                    AnchorNode anchorNode = new AnchorNode(anchor);
                    anchorNode.setParent(arFragment.getArSceneView().getScene());


                    TransformableNode tree = new TransformableNode(arFragment.getTransformationSystem());
                    tree.setParent(anchorNode);

                    tree.setRenderable(treeRenderable);
                    tree.select();
                    tree.setOnTapListener((HitTestResult hitTestResult, MotionEvent Event) ->

                    {

                        Node nodeToRemove = hitTestResult.getNode();
                        anchorNode.removeChild(nodeToRemove );

                        TransformableNode tree2 = new TransformableNode(arFragment.getTransformationSystem());
                        tree2.setParent(anchorNode);

                        tree2.setRenderable(treeRenderable2);
                        tree2.select();
                    });
                });


    }
}