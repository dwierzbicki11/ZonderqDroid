package pl.zonderq.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalTime
import java.time.format.DateTimeFormatter

private val White = Color(0xFFFAFAFF)
private val Muted = Color(0xFFAFB7D4)
private val Cyan = Color(0xFF6DDEF0)
private val Violet = Color(0xFF9379FA)
private val Navy = Color(0xFF0A0E23)
private val CardColor = Color(0xFF212946)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { ZonderqApp() }
    }
}

@Composable
private fun ZonderqApp() {
    var selectedPage by rememberSaveable { mutableIntStateOf(0) }
    MaterialTheme(colorScheme = darkColorScheme(primary = Cyan, secondary = Violet)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(Color(0xFF19224F), Navy, Color(0xFF11172F))))
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 18.dp, vertical = 8.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                Text("ZONDERQ", color = White, fontWeight = FontWeight.Black, fontSize = 19.sp,
                    letterSpacing = 2.sp)
                Text("UI ${UiDesign.version}  •  PREVIEW", color = Cyan, fontSize = 10.sp)
            }
            Spacer(Modifier.height(14.dp))
            Box(modifier = Modifier.weight(1f)) {
                when (selectedPage) {
                    0 -> HomeScreen()
                    1 -> ControlCenter()
                    else -> LockScreenDemo()
                }
            }
            Spacer(Modifier.height(10.dp))
            Surface(color = Color.White.copy(alpha = 0.09f), shape = RoundedCornerShape(28.dp),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.12f))) {
                Row(Modifier.fillMaxWidth().padding(5.dp), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    listOf("⌂  Pulpit", "◉  Sterowanie", "◷  Blokada").forEachIndexed { index, title ->
                        Box(
                            Modifier.weight(1f)
                                .clip(RoundedCornerShape(22.dp))
                                .background(if (selectedPage == index) Violet.copy(alpha = 0.48f) else Color.Transparent)
                                .clickable { selectedPage = index }
                                .padding(vertical = 13.dp),
                            contentAlignment = Alignment.Center
                        ) { Text(title, color = White, fontSize = 11.sp, maxLines = 1) }
                    }
                }
            }
        }
    }
}

@Composable
private fun GlassCard(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Surface(
        modifier = modifier,
        color = CardColor.copy(alpha = 0.77f),
        shape = RoundedCornerShape(UiDesign.cornerDp.dp),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.12f))
    ) { Box(Modifier.padding(16.dp)) { content() } }
}

private data class AppEntry(val label: String, val packageName: String, val activityName: String)

@Composable
private fun HomeScreen() {
    val context = LocalContext.current
    val apps = remember(context) {
        val intent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
        context.packageManager.queryIntentActivities(intent, PackageManager.ResolveInfoFlags.of(0L))
            .filter { it.activityInfo.packageName != context.packageName }
            .map { AppEntry(it.loadLabel(context.packageManager).toString(),
                it.activityInfo.packageName, it.activityInfo.name) }
            .distinctBy { it.packageName to it.activityName }
            .sortedBy { it.label.lowercase() }
            .take(UiDesign.maxVisibleApps)
    }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Spacer(Modifier.height(18.dp))
        Text(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")), fontSize = 66.sp,
            color = White, fontWeight = FontWeight.Light, lineHeight = 72.sp)
        Text("Twój Android. Twoje zasady.", color = Muted, fontSize = 14.sp)
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            GlassCard(Modifier.weight(1f)) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("☀  Pogoda", color = Muted, fontSize = 12.sp)
                    Text("21°", color = White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                    Text("Dane demonstracyjne", color = Muted, fontSize = 10.sp)
                }
            }
            GlassCard(Modifier.weight(1f)) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("✦  Zonderq", color = Muted, fontSize = 12.sp)
                    Text("UI 0.1", color = Cyan, fontSize = 27.sp, fontWeight = FontWeight.Bold)
                    Text("Design prototype", color = Muted, fontSize = 10.sp)
                }
            }
        }
        Text("APLIKACJE", color = Muted, fontSize = 12.sp, fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp, modifier = Modifier.padding(top = 8.dp))
        if (apps.isEmpty()) {
            Text("Brak widocznych aplikacji. Sprawdź uprawnienia widoczności pakietów.", color = Muted)
        }
        apps.chunked(UiDesign.appGridColumns).forEach { group ->
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                group.forEach { app ->
                    Column(Modifier.width(72.dp).clickable { launchApp(context, app) },
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(Modifier.size(52.dp).clip(RoundedCornerShape(17.dp))
                            .background(Brush.linearGradient(listOf(Violet, Cyan))),
                            contentAlignment = Alignment.Center) {
                            Text(app.label.take(1).uppercase(), color = Navy,
                                fontWeight = FontWeight.Black, fontSize = 23.sp)
                        }
                        Spacer(Modifier.height(6.dp))
                        Text(app.label, color = White, fontSize = 10.sp, maxLines = 1,
                            overflow = TextOverflow.Ellipsis, textAlign = TextAlign.Center)
                    }
                }
                repeat(UiDesign.appGridColumns - group.size) { Spacer(Modifier.width(72.dp)) }
            }
        }
        Text("Pierwsza wersja launchera • maks. 12 aplikacji", color = Muted, fontSize = 11.sp)
    }
}

private fun launchApp(context: Context, entry: AppEntry) {
    try {
        val intent = Intent.makeMainActivity(ComponentName(entry.packageName, entry.activityName))
        context.startActivity(intent)
    } catch (_: Exception) {
        Toast.makeText(context, "Nie można uruchomić: ${entry.label}", Toast.LENGTH_SHORT).show()
    }
}

@Composable
private fun ControlTile(title: String, symbol: String, enabled: Boolean, onClick: () -> Unit,
    modifier: Modifier = Modifier) {
    Surface(modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(UiDesign.cornerDp.dp),
        color = if (enabled) Violet.copy(alpha = 0.75f) else CardColor,
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.14f))) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(15.dp)) {
            Text(symbol, color = White, fontSize = 27.sp)
            Column {
                Text(title, color = White, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                Text(if (enabled) "Włączone (demo)" else "Wyłączone (demo)", color = Muted, fontSize = 11.sp)
            }
        }
    }
}

@Composable
private fun ControlCenter() {
    var wifi by rememberSaveable { mutableStateOf(true) }
    var bluetooth by rememberSaveable { mutableStateOf(false) }
    var mobile by rememberSaveable { mutableStateOf(true) }
    var airplane by rememberSaveable { mutableStateOf(false) }
    var brightness by rememberSaveable { mutableFloatStateOf(0.65f) }
    var volume by rememberSaveable { mutableFloatStateOf(0.5f) }

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Spacer(Modifier.height(12.dp))
        Text("Centrum sterowania", color = White, fontSize = 27.sp, fontWeight = FontWeight.Bold)
        Text("Koncepcja Zonderq Control • tylko podgląd", color = Muted, fontSize = 12.sp)
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            ControlTile("Wi-Fi", "◉", wifi, { wifi = !wifi }, Modifier.weight(1f))
            ControlTile("Bluetooth", "ᛒ", bluetooth, { bluetooth = !bluetooth }, Modifier.weight(1f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            ControlTile("Sieć komórkowa", "▥", mobile, { mobile = !mobile }, Modifier.weight(1f))
            ControlTile("Samolot", "✈", airplane, { airplane = !airplane }, Modifier.weight(1f))
        }
        GlassCard(Modifier.fillMaxWidth()) {
            Column {
                Text("☀  Jasność — demo", color = White, fontWeight = FontWeight.SemiBold)
                Slider(value = brightness, onValueChange = { brightness = it })
                Text("${(brightness * 100).toInt()}% • nie zmienia jasności telefonu", color = Muted, fontSize = 11.sp)
            }
        }
        GlassCard(Modifier.fillMaxWidth()) {
            Column {
                Text("♫  Głośność — demo", color = White, fontWeight = FontWeight.SemiBold)
                Slider(value = volume, onValueChange = { volume = it })
                Text("${(volume * 100).toInt()}% • tylko wizualizacja", color = Muted, fontSize = 11.sp)
            }
        }
        Text("Przełączniki nie sterują rzeczywistym Wi-Fi ani radiem telefonu.",
            color = Muted, fontSize = 11.sp)
    }
}

@Composable
private fun LockScreenDemo() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(13.dp)) {
            Text("◌", color = Cyan, fontSize = 36.sp)
            Text(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")),
                color = White, fontSize = 76.sp, fontWeight = FontWeight.ExtraLight, lineHeight = 82.sp)
            Text("Zonderq • Twój własny styl", color = Muted, fontSize = 16.sp)
            Spacer(Modifier.height(28.dp))
            GlassCard(Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("✦  Zonderq UI", color = Cyan, fontWeight = FontWeight.Bold)
                    Text("Tu pojawią się karty powiadomień, widżety i animacje.",
                        color = White, fontSize = 13.sp)
                    Text("To ekran demonstracyjny, a nie prawdziwy ekran blokady.",
                        color = Muted, fontSize = 11.sp)
                }
            }
        }
    }
}
