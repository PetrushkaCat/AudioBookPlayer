package cat.petrushkacat.audiobookplayer.app.ui.components.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cat.petrushkacat.audiobookplayer.core.components.shared.bookdropdownmenu.BookDropDownMenuComponent

@Composable
fun BookDropdownMenuComponentUi(
    component: BookDropDownMenuComponent,
    expanded: MutableState<Boolean>,
    modifier: Modifier = Modifier
) {
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
        ) {
            Column(modifier = Modifier.background(color = Color.Gray)) {
                DropDownItem("name", {})
                DropDownItem("name", {})
                DropDownItem("name", {})
            }

        }
}

@Composable
fun DropDownItem(name: String, onClick: () -> Unit) {

    Column(
        modifier = Modifier.height(50.dp).width(150.dp).clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = name,
            modifier = Modifier,
            textAlign = TextAlign.Center,
            style = TextStyle(fontSize = 16.sp)
        )
    }
}