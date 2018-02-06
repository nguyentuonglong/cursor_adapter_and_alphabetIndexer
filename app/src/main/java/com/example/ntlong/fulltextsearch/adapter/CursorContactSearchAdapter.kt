package com.example.ntlong.fulltextsearch.adapter

import android.database.Cursor
import android.provider.ContactsContract
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AlphabetIndexer
import android.widget.SectionIndexer
import android.widget.TextView
import com.example.ntlong.fulltextsearch.R

/*
 * Created by ntlong on 11/29/17.
 */

class CursorContactSearchAdapter(cursor: Cursor?) : CursorRecyclerViewAdapter<CursorContactSearchAdapter.ViewHolder>(cursor), SectionIndexer {


    private lateinit var mAlphabetIndexer: AlphabetIndexer

    override fun onBindViewHolder(viewHolder: ViewHolder, cursor: Cursor, position: Int) {

        viewHolder.renderData(cursor, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_contact_list, parent, false)
        return ViewHolder(itemView)
    }

    override fun getSections(): Array<Any> {
        return mAlphabetIndexer.sections
    }

    override fun getSectionForPosition(position: Int): Int {
        return mAlphabetIndexer.getSectionForPosition(position)
    }

    override fun getPositionForSection(sectionIndex: Int): Int {
        return mAlphabetIndexer.getPositionForSection(sectionIndex)
    }

    override fun swapCursor(newCursor: Cursor?): Cursor? {

        newCursor?.let {
            mAlphabetIndexer = AlphabetIndexer(newCursor,
                    newCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME),
                    " ABCDEFGHIJKLMNOPQRTSUVWXYZ")
            mAlphabetIndexer.setCursor(newCursor)


        }
        return super.swapCursor(newCursor)
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var nameView: TextView = itemView.findViewById(R.id.name) as TextView
        private var indexView: TextView = itemView.findViewById(R.id.index) as TextView


        private lateinit var name: String


        internal fun renderData(cursor: Cursor, position: Int) {

            cursor.moveToPosition(position)

            //get data from cursor
            name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))

            nameView.text = name

            val sec = getSectionForPosition(position)
            val pos = getPositionForSection(sec)

            if (position == pos) {
                indexView.visibility = View.VISIBLE
                indexView.text = Character.toString(getFirstChar(name))
            } else {
                indexView.visibility = View.GONE
            }
        }

    }

    fun getFirstChar(name: String): Char {
        return if (TextUtils.isEmpty(name)) {
            '#'
        } else {
            val c = Character.toUpperCase(name[0])

            if (Character.isAlphabetic(c.toInt())) {
                c;
            } else {
                '#';
            }
        }
    }
}
