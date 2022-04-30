package com.lbw.privacykeeper.ui.user

import android.app.AlertDialog
import android.content.res.Configuration
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lbw.privacykeeper.ui.theme.PrivacyKeeperTheme
import privacykeeperv1.R

@Composable
fun ConfirmUpdateDialog(
    openDialog : Boolean,
    closeDialog: ()->Unit
) {
    if(openDialog){
        AlertDialog(
            modifier = Modifier.clip(RoundedCornerShape(10.dp)),
            onDismissRequest = {
                closeDialog()
            },
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_warning_foreground),
                    contentDescription = null
                )
            },
            title = {
                Text(text = stringResource(id = R.string.warning))
            },
            text = {
                Text(
                    text = stringResource(id = R.string.confirm_dialog_text),

                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        closeDialog()
                        //TODO: 打开身份验证
                    }
                ) {
                    Text(text = stringResource(id = R.string.confirm))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        closeDialog()
                    }
                ) {
                    Text(text = stringResource(id = R.string.dismiss))
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewConfirmDialog() {
    PrivacyKeeperTheme {
        val viewModel : UserViewModel = viewModel()
        ConfirmUpdateDialog(
            viewModel.showConfirmDialog
        ) { viewModel.closeConfirmDialog() }
    }
}