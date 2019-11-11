package edu.bluejack19_1.KumVulanDFreelancer.recycleView.item

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import edu.bluejack19_1.KumVulanDFreelancer.R


class personItem : Fragment() {

    companion object {
        fun newInstance() = personItem()
    }

    private lateinit var viewModel: PersonItemViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.person_item_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(PersonItemViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
