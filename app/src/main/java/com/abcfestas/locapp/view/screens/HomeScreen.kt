package com.abcfestas.locapp.view.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.abcfestas.locapp.R
import com.abcfestas.locapp.ui.theme.Gray
import com.abcfestas.locapp.ui.theme.Green700
import com.abcfestas.locapp.ui.theme.MainColor
import com.abcfestas.locapp.ui.theme.Red
import com.abcfestas.locapp.ui.theme.Typography
import com.abcfestas.locapp.ui.theme.Yellow
import com.abcfestas.locapp.view.components.CircleImage
import com.abcfestas.locapp.view.components.LayoutDefault
import compose.icons.FontAwesomeIcons
import compose.icons.Octicons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Phone
import compose.icons.octicons.ChevronDown16
import compose.icons.octicons.ChevronUp16
import java.util.Locale

@Composable
fun HomeScreen (
    navController: NavController
) {
    LayoutDefault(
        content = {
            FinancialCard()
            CurrentRentals()
            UpcomingRentals()
        },
        actionTitle = stringResource(id = R.string.new_rent),
        onClickAction = {
            Log.d("home", "clicked")
        }
    )
}

@Composable
fun FinancialCard() {
    // TODO: get dynamic data
    val percentage = 25
    
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        colors = CardDefaults.elevatedCardColors(
            containerColor = Green700,
            contentColor = Color.White
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Faturamento",
                style = Typography.bodyMedium,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "R$ 1.856,78",
                    style = Typography.titleLarge,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(2.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (percentage > 0) { Octicons.ChevronUp16 } else { Octicons.ChevronDown16 },
                            contentDescription = if (percentage > 0) { "Up" } else { "Down" },
                            tint = if (percentage > 0) { Green700 } else { Red },
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "${percentage}%",
                            style = Typography.displaySmall
                        )
                    }
                }
            }
            /*Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .background(
                        color = Green800,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(4.dp, 8.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(imageVector = Octicons.Graph16, contentDescription = "Months")
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Gráficos", style = Typography.titleMedium, color = Color.White, fontSize = 11.sp)
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(imageVector = Octicons.Graph16, contentDescription = "Months")
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Gráficos", style = Typography.titleMedium, color = Color.White, fontSize = 11.sp)
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(imageVector = Octicons.Graph16, contentDescription = "Months")
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Gráficos", style = Typography.titleMedium, color = Color.White, fontSize = 11.sp)
                    }
                }
            }*/
        }
    }
}

@Composable
fun RentScheduleBox(
    customerName: String = "Adriana",
    rentTitle: String = "3 mesas + 4 cadeiras rosas + 1 mesa 2m + 50 toalhas rosas + 1 tapete 2x2m",
    day: String = "Sex",
    remainingDays: Int = 2,
    rented: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Column(
            modifier = Modifier
                .width(20.dp)
                .wrapContentWidth(align = Alignment.Start)
        ) {
            Text(text = day, style = Typography.displayMedium, color = Gray)
            if (remainingDays > 0) {
                Text(text = "${remainingDays}d", style = Typography.displayMedium, color = Gray)
            } else if(remainingDays < 0) {
                Text(text = "${remainingDays}d", style = Typography.displayMedium, color = Red)
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Box(
                modifier = Modifier
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(8.dp),
                    )
                    .border(width = 0.65.dp, color = Green700, shape = RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                Column {
                    // Rent's status
                    if (rented && remainingDays >= 0) {
                        Text(
                            text = "Alugado".uppercase(Locale.ROOT),
                            color = Green700,
                            style = Typography.displayMedium,
                            fontWeight = FontWeight.ExtraBold
                        )
                    } else if(rented && remainingDays < 0) {
                        Text(
                            text = "Atrasado".uppercase(Locale.ROOT),
                            color = Red,
                            style = Typography.displayMedium,
                            fontWeight = FontWeight.ExtraBold
                        )
                    } else if (!rented && remainingDays >= 0) {
                        Text(
                            text = "Agendado".uppercase(Locale.ROOT),
                            color = Yellow,
                            style = Typography.displayMedium,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    // Rent's title
                    Text(
                        text = rentTitle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = Typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    // Customre line
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = customerName, style = Typography.displayMedium)
                        Icon(
                            modifier = Modifier
                                .size(14.dp)
                                .clickable {
                                    Log.d("TEST", "CLICK TO CALL")
                                },
                            imageVector = FontAwesomeIcons.Solid.Phone,
                            contentDescription = "Phone"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CurrentRentals() {
    Spacer(modifier = Modifier.height(16.dp))
    Text(text = stringResource(R.string.current_rentals), style = Typography.titleLarge)
    Spacer(modifier = Modifier.height(16.dp))

    /*RentScheduleBox()
    Spacer(modifier = Modifier.height(16.dp))
    RentScheduleBox(
        customerName = "Josefina Marques",
        rented = true,
        day = "Dom",
        remainingDays = -1
    )
    Spacer(modifier = Modifier.height(16.dp))
    RentScheduleBox(
        customerName = "Josefina Marques",
        rented = true,
        day = "Seg",
        remainingDays = 0
    )
    Spacer(modifier = Modifier.height(16.dp))*/


    RentBox(
        title = "Adriana",
        subtitle = "1 dia em atraso",
        progress = -0.1f
    )

    RentBox(
        title = "Ítalo",
        subtitle = "3 dias em atraso",
        progress = -0.3f,
        imageResource = R.drawable.product_example
    )

    RentBox(
        title = "Diana de Carvalho",
        subtitle = "Notas...",
        progress = 0.83f,
        imageResource = R.drawable.product_example
    )
    RentBox(
        title = "José da Silva",
        subtitle = "lembrar de pedir algo importante para este cliente",
        progress = 0.36f,
        imageResource = R.drawable.product_example
    )
    RentBox(
        title = "Marcos",
        subtitle = "",
        progress = 0.22f,
        imageResource = R.drawable.product_example
    )
}

@Composable
fun UpcomingRentals() {
    Spacer(modifier = Modifier.height(16.dp))
    Text(text = stringResource(id = R.string.upcoming_rentals), style = Typography.titleLarge)
    Spacer(modifier = Modifier.height(16.dp))

    RentBox(
        title = "Marcos",
        subtitle = "Reservado para 20/10/2020"
    )

    RentBox(
        title = "Mariana Pereira",
        subtitle = "Reservado para 20/10/2020",
        imageResource = R.drawable.product_example
    )

    RentBox(
        title = "Mariana Pereira",
        subtitle = "Reservado para 20/10/2020"
    )

    RentBox(
        title = "Mariana Pereira",
        subtitle = "Reservado para 20/10/2020"
    )
}

@Composable
fun RentBox(
    title: String,
    subtitle: String,
    progress: Float? = null,
    imageResource: Int? = null
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        CircleImage(
            imageResource = imageResource,
            contentDescription = "Image"
        )

        Column(modifier = Modifier.padding(start = 24.dp)) {
            Text(text = title, style = Typography.bodyLarge)
            Text(text = subtitle, style = Typography.bodyMedium)
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    if (progress != null) {
        LinearProgressIndicator(
            progress = { if (progress < 0) { 1f } else { progress } },
            modifier = Modifier
                .height(6.dp)
                .clip(RoundedCornerShape(16.dp))
                .fillMaxWidth(),
            color = if (progress < 0) { Red } else { MainColor },
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()
    HomeScreen(navController)
}