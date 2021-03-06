package com.example.bolovanje.ui.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bolovanje.R
import com.example.bolovanje.model.Employer
import com.example.bolovanje.utils.DateUtils.Companion.convertDatesToCalendarObj
import kotlinx.android.synthetic.main.employers_search_row.view.*
import java.util.*
import kotlin.collections.ArrayList

class SearchAdapter (private val context: Context,
                     var employerList: ArrayList<Employer>,
                     val selectedDates: MutableList<Calendar>,
                     val onSearchItemClickListener: OnSearchItemClickListener):
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.employers_search_row, parent, false)
        return ViewHolder(itemView)

    }

    override fun getItemCount(): Int {
        return employerList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(employerList[position], position)
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bindItem(employer: Employer, position: Int){
            itemView.txtEmployers.text = "${employer.firstName} ${employer.lastName}"
            itemView.txtNumberOfDays.text = employer.numOfDays.toString()
            itemView.txtTenOrMoreDays.text = employer.daysThisMonthNum.toString()
            itemView.txtDaysWithExcuse.text = employer.daysWithExcuseNum.toString()
            itemView.txtDaysWithoutExcuse.text = employer.daysWithoutExcuseNum.toString()
            itemView.txtDaysThisMonth.text = ""
            itemView.txtDaysWithoutExcuseList.text = ""

            var listOfDaysThisMonth: String = ""
            var listOfDaysWithoutExcuse = ""

            if(employer.daysThisMonthList.isNotEmpty()){
                employer.daysThisMonthList.sorted().forEach {
                    listOfDaysThisMonth +=  "${it.substring(0,6)} "
                }
                itemView.txtDaysThisMonth.text = listOfDaysThisMonth
            }else{
                itemView.txtDaysThisMonth.text = context.getString(R.string.no_sick_leave)
            }

            if(employer.daysWithoutExcuseList.isNotEmpty()){
                employer.daysWithoutExcuseList.sorted().forEach {
                    listOfDaysWithoutExcuse +=  "${it.substring(0,6)} "
                }
                itemView.txtDaysWithoutExcuseList.text = listOfDaysWithoutExcuse
            }else{
                itemView.txtDaysWithoutExcuseList.text = context.getString(R.string.no_days_without_excuse)
            }

            itemView.btnRemove.setOnClickListener {
                onSearchItemClickListener.onDeleteClick(position)
            }

            itemView.btnEdit.setOnClickListener {
                onSearchItemClickListener.onEditClick(position, employer.firstName!!, employer.lastName!!, convertDatesToCalendarObj(employer.daysWithExcuseList))
            }

            itemView.txtAddDaysWithoutExcuse.setOnClickListener{
                onSearchItemClickListener.onUpdateDaysWithoutExcuseClick(position, convertDatesToCalendarObj(employer.daysWithoutExcuseList))
            }

            itemView.txtDaysWithExcuseShow.setOnClickListener {
                onSearchItemClickListener.onShowClick(employer.daysWithExcuseList)
            }

        }
    }

}