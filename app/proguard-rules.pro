# ===================================================================
# CONFIGURAÇÕES GERAIS E LOGS
# ===================================================================
# Mantém informações de linhas e arquivos para ajudar no Crashlytics / Debug
-keepattributes SourceFile,LineNumberTable
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod

# ===================================================================
# RETROFIT & OKHTTP
# ===================================================================
-keepattributes RuntimeVisibleAnnotations,RuntimeInvisibleAnnotations
-keepclassmembers,allowobfuscation class * {
    @retrofit2.http.* <methods>;
}
-dontwarn retrofit2.**
-dontwarn okhttp3.**
-dontwarn okio.**

# ===================================================================
# DOMAIN / MODELS (CRÍTICO PARA SERIALIZAÇÃO)
# ===================================================================
# Impede a ofuscação dos seus Models de dados (DTOs/Domain) para não quebrar o JSON
-keep class br.com.samantaalbanez.moviescatalog.domain.model.** { *; }
-keep class br.com.samantaalbanez.moviescatalog.data.remote.model.** { *; }
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# ===================================================================
# HILT / DAGGER
# ===================================================================
-keep class dagger.hilt.** { *; }
-keep class * extends dagger.hilt.internal.UnsafeCasts { *; }
-keep class * extends dagger.hilt.internal.GeneratedComponent { *; }
-keep class * extends dagger.hilt.internal.GeneratedComponentManager { *; }

-dontwarn dagger.hilt.**
-dontwarn com.google.dagger.hilt.**

# ===================================================================
# JETPACK COMPOSE & PAGING
# ===================================================================
-keep class androidx.compose.runtime.** { *; }
-keep class androidx.paging.** { *; }

# ===================================================================
# BUILDCONFIG
# ===================================================================
# Preserva a classe BuildConfig para não perder variáveis como API_TOKEN
-keep class br.com.samantaalbanez.moviescatalog.BuildConfig { *; }
