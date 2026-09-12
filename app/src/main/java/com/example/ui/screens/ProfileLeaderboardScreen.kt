package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.UserProfileEntity
import com.example.data.model.LeaderboardUser
import com.example.ui.theme.*

@Composable
fun ProfileLeaderboardScreen(
    userProfile: UserProfileEntity?,
    solvedCount: Int,
    totalProblemsCount: Int,
    leaderboardUsers: List<LeaderboardUser>,
    cfHandleInput: String,
    onCfHandleInputChange: (String) -> Unit,
    onLinkHandle: () -> Unit,
    cfLinkStatus: String?,
    isCfLinking: Boolean,
    modifier: Modifier = Modifier
) {
    val rating = userProfile?.rating ?: 1240
    val ratingDivision = when {
        rating < 1200 -> "Newbie (مبتدئ)" to CfNewbie
        rating < 1400 -> "Pupil (تلميذ صاعد)" to CfPupil
        rating < 1600 -> "Specialist (متخصص)" to CfSpecialist
        rating < 1900 -> "Expert (خبير)" to CfExpert
        else -> "Candidate Master (أستاذ مرشح)" to CfCandidateMaster
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. Personal Profile & Rating Card
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, ratingDivision.second.copy(alpha = 0.4f), RoundedCornerShape(18.dp))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.linearGradient(
                                            listOf(ratingDivision.second, BrandBlueDark)
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = userProfile?.handle?.take(2)?.uppercase() ?: "CF",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = userProfile?.handle ?: "Codeforces_Hero",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = Color.White
                                )
                                Text(
                                    text = ratingDivision.first,
                                    color = ratingDivision.second,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        // Rating number
                        Surface(
                            color = ratingDivision.second,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "$rating",
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.Black,
                                    fontSize = 18.sp
                                )
                                Text(
                                    text = "التقييم",
                                    fontSize = 10.sp,
                                    color = Color.Black,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Metrics Grid (XP, Level, Solved, Streak)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        MetricBadge(
                            title = "المسائل المحلولة",
                            value = "$solvedCount / $totalProblemsCount",
                            icon = Icons.Default.CheckCircle,
                            color = Color(0xFF10B981),
                            modifier = Modifier.weight(1f)
                        )
                        MetricBadge(
                            title = "نقاط الخبرة XP",
                            value = "${userProfile?.xp ?: 650}",
                            icon = Icons.Default.Bolt,
                            color = BrandAmber,
                            modifier = Modifier.weight(1f)
                        )
                        MetricBadge(
                            title = "تتابع الأيام",
                            value = "${userProfile?.streakDays ?: 5} أيام 🔥",
                            icon = Icons.Default.LocalFireDepartment,
                            color = Color(0xFFF97316),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // 2. Community Comparison Stats
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "📊 مقارنة مستواك بالمجتمع البرمجي",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    val percentile = (65 + (solvedCount * 4)).coerceAtMost(99)
                    Text(
                        text = "تقييمك الحالي أعلى من $percentile% من المتسابقين في المنصة!",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = { percentile / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "المستوى المستهدف التالي: الوصول إلى 1400 (Specialist 🚀) لحل مسائل DP ومصفوفات البادئات.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // 3. Link Real Codeforces Handle via API
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BrandBlueDark.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Link, contentDescription = null, tint = BrandCyan)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "ربط حسابك بموقع Codeforces الرسمي",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "أدخل اسم المستخدم (Handle) لجلب تقييمك الحقيقي وتصنيفك الدولي مباشرة من سيرفرات كود فورس.",
                        color = Color(0xFF94A3B8),
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = cfHandleInput,
                            onValueChange = onCfHandleInputChange,
                            placeholder = { Text("مثال: tourist أو اسمك", fontSize = 12.sp) },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = onLinkHandle,
                            enabled = !isCfLinking && cfHandleInput.isNotBlank(),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            if (isCfLinking) {
                                CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                            } else {
                                Text("تحقق وربط", fontSize = 12.sp)
                            }
                        }
                    }

                    if (cfLinkStatus != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = cfLinkStatus,
                            color = if (cfLinkStatus.contains("بنجاح")) Color(0xFF10B981) else Color(0xFFEF4444),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // 4. Interactive Leaderboard (Community Ranking)
        item {
            Text(
                text = "🏆 لوحة المتصدرين للمجتمع العربي (Leaderboard)",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        items(leaderboardUsers, key = { it.handle }) { user ->
            val isCurrent = user.isCurrentUser
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (isCurrent) Color(0xFF1E3A8A).copy(alpha = 0.4f)
                    else MaterialTheme.colorScheme.surface
                ),
                border = if (isCurrent) BorderStroke(1.5.dp, BrandCyan) else null,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Rank badge
                    Surface(
                        color = when (user.rank) {
                            1 -> BrandAmber
                            2 -> Color(0xFF94A3B8)
                            3 -> Color(0xFFB45309)
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        },
                        shape = CircleShape,
                        modifier = Modifier.size(30.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "${user.rank}",
                                fontWeight = FontWeight.Bold,
                                color = if (user.rank <= 3) Color.Black else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(text = user.countryFlag, fontSize = 16.sp)

                    Spacer(modifier = Modifier.width(8.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = user.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            if (isCurrent) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Surface(
                                    color = BrandCyan,
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = "أنت",
                                        color = Color.Black,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                    )
                                }
                            }
                        }
                        Text(
                            text = "@${user.handle} • ${user.badge}",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "${user.rating}",
                            fontWeight = FontWeight.Bold,
                            color = BrandCyan,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "${user.solvedCount} محلولة",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
fun MetricBadge(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Color(0xFF0F172A),
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 13.sp
            )
            Text(
                text = title,
                fontSize = 10.sp,
                color = Color(0xFF94A3B8)
            )
        }
    }
}
