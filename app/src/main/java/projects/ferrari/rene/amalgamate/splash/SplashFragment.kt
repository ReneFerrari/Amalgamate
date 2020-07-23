package projects.ferrari.rene.amalgamate.splash

import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.commitNow
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import kotlinx.android.synthetic.main.activity_start.ivLogoAnimation
import kotlinx.android.synthetic.main.fragment_splash.*
import projects.ferrari.rene.amalgamate.R

class SplashFragment : Fragment(R.layout.fragment_splash) {
    private var logoAnimationCallback: Animatable2Compat.AnimationCallback? = null
    private var drawableLogo: Drawable? = null

    private companion object {
        const val EXIT_ANIM_DURATION_MS = 300L
        const val EXIT_ANIM_DELAY_MS = 100L
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        drawableLogo = ivLogoAnimation.drawable
        setFullscreen()
        animateLogo()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterLogoAnimationCallback()
    }

    private fun animateLogo() {
        registerLogoAnimationCallback()
        (drawableLogo as? Animatable)?.start()
    }

    private fun registerLogoAnimationCallback() {
        logoAnimationCallback = getLogoAnimationCallback()
        AnimatedVectorDrawableCompat.registerAnimationCallback(
            drawableLogo,
            logoAnimationCallback
        )
    }

    private fun unregisterLogoAnimationCallback() {
        logoAnimationCallback?.let { callback ->
            AnimatedVectorDrawableCompat.unregisterAnimationCallback(drawableLogo, callback)
        }
    }

    private fun getLogoAnimationCallback() = object : Animatable2Compat.AnimationCallback() {
        override fun onAnimationEnd(drawable: Drawable?) {
            super.onAnimationEnd(drawable)
            AnimatedVectorDrawableCompat.unregisterAnimationCallback(drawable, this)
            removeFragment()
        }
    }

    private fun removeFragment() {
        animateFragmentExit()
        (activity as? OnRemovedListener)?.onSplashFragmentRemoved()
        resetFullscreen()
    }

    /*
    Fades out Fragment. Since it is laid on top of another one (transaction add),
    the Fragment beneath will be visible.

    THe manual animation is used in favor of the FragmentTransaction's setCustomAnimation, because
    setting the animation in the transaction does not work for this case.
     */
    private fun animateFragmentExit() {
        flContainer.animate()
            .alpha(0f)
            .apply {
                duration =
                    EXIT_ANIM_DURATION_MS
                startDelay =
                    EXIT_ANIM_DELAY_MS
            }
            .withEndAction {
                parentFragmentManager.commitNow {
                    remove(this@SplashFragment)
                }
            }
            .start()
    }

    private fun setFullscreen() {
        activity?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    private fun resetFullscreen() {
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    interface OnRemovedListener {
        fun onSplashFragmentRemoved()
    }
}
