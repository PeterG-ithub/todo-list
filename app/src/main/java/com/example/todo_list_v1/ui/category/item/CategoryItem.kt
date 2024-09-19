package com.example.todo_list_v1.ui.category.item

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todo_list_v1.R
import com.example.todo_list_v1.data.category.Category
import com.example.todo_list_v1.ui.theme.Todolistv1Theme

@Composable
fun CategoryItem(
    modifier: Modifier = Modifier,
    category: Category,
    visible: Boolean = true,
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = category.name,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.widthIn(max = 225.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = if(visible) painterResource(id = R.drawable.visible_on) else painterResource(
                    id = R.drawable.visible_off
                ) ,
                contentDescription = "Visible On"
            )
            Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small)))
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete Category",
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryItemPreview() {
    Todolistv1Theme {
        CategoryItem(
            category = Category(name = "Category 1")
        )
    }
}