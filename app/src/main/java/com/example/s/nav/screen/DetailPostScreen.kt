package com.example.s.nav.screen
//abre as imgens e pode navegar
/**@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DetailScreen(/*navController: NavController, main: Activity, modifier: Modifier =
    Modifier, sportType: String, gameName: String*/
) {
    val ps = remember {
        //DataProvider.post
    }
    val db = Firebase.firestore
    var li = emptyList<String>()
    val docRef = db.collection("Users").document("FDSEUTOme7OsJL2Fea8f")
    docRef.get().addOnSuccessListener { document ->
        if (document != null) {
            var user = document.toObject(User::class.java)
            for (f in user?.friends!!){
                Log.d("friend", f)
            }
        }
    }

    val pageState = rememberPagerState(pageCount = { 2 })

    Box(modifier = Modifier.fillMaxSize()){
        HorizontalPager(
            state = pageState
        ) { index ->
            if (ps.images != null){
                Image(painter = painterResource(id = ps.images[index]),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize())
            }


        }
        Row(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pageState.pageCount) { iteration ->
                val color = if (pageState.currentPage == iteration) Color.DarkGray else Color.LightGray
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(16.dp)
                )
            }
        }
    }


}*/