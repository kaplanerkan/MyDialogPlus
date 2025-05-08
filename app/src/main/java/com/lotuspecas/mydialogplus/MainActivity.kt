package com.lotuspecas.mydialogplus

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.lotuspecas.mydialogplus.databinding.ActivityMainBinding
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.GridHolder
import com.orhanobut.dialogplus.Holder
import com.orhanobut.dialogplus.ListHolder
import com.orhanobut.dialogplus.ViewHolder
import es.dmoral.toasty.Toasty

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()

    }

    private fun initViews() {

        binding.showDialogButton.setOnClickListener {
            showDialogPlus()
        }

    }

    private fun showDialogPlus() {

        //Ayarlari al
        val holderId = binding.holderRadioGroup.checkedRadioButtonId
        val showHeader = binding.headerCheckBox.isChecked
        val showFooter = binding.footerCheckBox.isChecked
        val fixedHeader = binding.fixedHeaderCheckBox.isChecked
        val fixedFooter = binding.fixedFooterCheckBox.isChecked
        val expanded = binding.expandedCheckBox.isChecked

        val gravity: Int = when (binding.positionRadioGroup.checkedRadioButtonId) {
            R.id.topPosition -> Gravity.TOP
            R.id.centerPosition -> Gravity.CENTER
            else -> Gravity.BOTTOM
        }

        val isGrid: Boolean
        val holder: Holder
        when (holderId) {
            R.id.basic_holder_radio_button -> {
                holder = ViewHolder(R.layout.content)
                isGrid = false
            }

            R.id.list_holder_radio_button -> {
                holder = ListHolder()
                isGrid = false
            }

            else -> {
                holder = GridHolder(3)
                isGrid = true
            }
        }

        val adapter =
            SimpleAdapter(this@MainActivity, isGrid, binding.listCountInput.text.toString().toInt())
        val builder = DialogPlus.newDialog(this).apply {
            setContentHolder(holder)

            val header = if (showHeader) R.layout.header else -1
            if (header != -1) {
                setHeader(R.layout.header, fixedHeader)
            }

            val footer = if (showFooter) R.layout.footer else -1
            if (footer != -1) {
                setFooter(R.layout.footer, fixedFooter)
            }

            setCancelable(true)
            setGravity(gravity)
            setAdapter(adapter)
            setOnClickListener { dialog, view ->
                if (view is TextView) {
                    toast(view.text.toString())
                }
                if (view is AppCompatButton) {
                    Log.e("TAG", "Item clicked: ${view.text}")
                    toast(view.text.toString())
                    if (view.text == "CLOSE") {
                        runOnUiThread { dialog.dismiss() }
                    }
                }


            }


            setOnItemClickListener { dialog, item, view, position ->
                try {
                    val textView = view.findViewById<TextView>(R.id.text_view)
                    toast(textView.text.toString())
                    Log.e("TAG", "Item clicked: $item")
                    val footerCloserButton =
                        view.findViewById<AppCompatButton>(R.id.footer_close_button)
                    footerCloserButton.setOnClickListener {
                        runOnUiThread { dialog.dismiss() }

                    }
                }catch (e: Exception){
                    toast("Error: ${e.message}")
                }

            }
            //        .setOnDismissListener(dismissListener)
            setExpanded(expanded)

            if (binding.contentHeightInput.text.toString().toInt() != -1) {
                setContentHeight(binding.contentHeightInput.text.toString().toInt())
            } else {
                setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
            }

            if (binding.contentWidthInput.text.toString().toInt() != -1) {
                setContentWidth(800)
            }

            setOnCancelListener { dialog -> toast("cancelled") }
            setOverlayBackgroundResource(android.R.color.transparent)
            //        .setContentBackgroundResource(R.drawable.corner_background)
            //                .setOutMostMargin(0, 100, 0, 0)
        }
        builder.create().show()
    }


    private fun toast(message: String) {
        Toasty.info(this@MainActivity, message, Toasty.LENGTH_SHORT).show()
    }

}