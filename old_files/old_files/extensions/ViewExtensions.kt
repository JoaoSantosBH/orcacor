package com.jomar.senhorpintor.extensions

import android.app.Activity
import android.content.Context
import android.view.Menu
import android.view.View
import android.view.View.*
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.jomar.senhorpintor.R
import com.jomar.senhorpintor.base.App
import com.squareup.picasso.Picasso

fun Fragment.findActivityNavViewController() =
        requireActivity().supportFragmentManager.fragments[0].findNavController()

fun AppCompatActivity.setToolbar(
        toolbarId: Int,
        titleString: String? = "",
        setUpNavigation: Boolean = false
) {
    setSupportActionBar(findViewById(toolbarId))
    supportActionBar?.setDisplayHomeAsUpEnabled(setUpNavigation)
    supportActionBar?.title = if (titleString == "") null else titleString
}

fun ImageView.loadImageUrl(url: String?, @DrawableRes placeHolder: Int? = R.color.blueAccent) {
    if (url?.isBlank() == true) {
        placeHolder?.let { this.setImageDrawable(ContextCompat.getDrawable(context, it)) }
        return
    }
    Picasso.get()
            .load(url)
            .let { placeHolder?.let { id -> it.placeholder(id).error(id) }; it }
            .fit()
            .error(R.drawable.bg_title_intro)
            .centerCrop()
            .into(this)
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Fragment.hideKeyboard() {
    view?.let {
        activity?.hideKeyboard(it)
    }
}

fun Fragment.setTitle(title: String) {
    (activity as AppCompatActivity).supportActionBar?.title = title


}

fun ViewPager.show() {
    this.apply {
        visibility = VISIBLE
    }
}

fun ViewPager.hide() {
    this.apply {
        visibility = INVISIBLE
    }
}

fun View.gone() {
    this.apply {
        visibility = GONE
    }
}


fun View.show() {
    this.apply {
        visibility = VISIBLE
    }
}

fun View.hide() {
    this.apply {
        visibility = INVISIBLE
    }
}

fun Menu.hideShowItem(menuItem: Int, visibility: Boolean) {
    this.findItem(menuItem).setVisible(visibility)
}

fun TextView.hideTitle() {
    this.visibility = GONE
}

fun TextView.showTitle() {
    this.visibility = VISIBLE
}

fun alternateImageButton(selected: Boolean): Boolean {
    when (selected) {
        true -> return false
        false -> return true
    }
}

fun showToastMessage(string: String) {
    return Toast.makeText(App.instance, string,Toast.LENGTH_SHORT).show()
}

fun NestedScrollView.addScrollListener(
        function: (Boolean) -> Unit
) {
    setOnScrollChangeListener { _: NestedScrollView?, _: Int, scrollY: Int, _: Int, oldScrollY: Int ->
        run {
            function(scrollY == (getChildAt(0).measuredHeight.minus(measuredHeight)) && scrollY > oldScrollY)
        }
    }
}


fun <T : RecyclerView.ViewHolder> T.listen(event: (position: Int, type: Int) -> Unit): T {
    itemView.setOnClickListener {
        event.invoke(getAdapterPosition(), getItemViewType())
    }
    return this
}

