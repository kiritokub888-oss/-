package com.example.reviewstudio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.reviewstudio.model.ReviewStudioStep
import com.example.reviewstudio.ui.components.*
import com.example.reviewstudio.ui.theme.AIReviewStudioTheme
import com.example.reviewstudio.ui.theme.DarkBackground
import com.example.reviewstudio.viewmodel.ReviewStudioViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: ReviewStudioViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AIReviewStudioTheme {
                val currentStep by viewModel.currentStep.collectAsState()
                val product by viewModel.product.collectAsState()
                val isExtracting by viewModel.isExtracting.collectAsState()
                val extractError by viewModel.extractError.collectAsState()
                val extractSuccess by viewModel.extractSuccess.collectAsState()
                val script by viewModel.script.collectAsState()
                val selectedTone by viewModel.selectedTone.collectAsState()
                val selectedDuration by viewModel.selectedDuration.collectAsState()
                val isGeneratingScript by viewModel.isGeneratingScript.collectAsState()
                val selectedAvatar by viewModel.selectedAvatar.collectAsState()
                val aspectRatio by viewModel.aspectRatio.collectAsState()
                val renderJob by viewModel.renderJob.collectAsState()

                var isApiDialogOpen by remember { mutableStateOf(false) }

                Scaffold(
                    topBar = {
                        Column {
                            HeaderBar(onOpenApiInfo = { isApiDialogOpen = true })
                            StepIndicator(
                                currentStep = currentStep,
                                onStepClick = { step -> viewModel.setStep(step) },
                                canNavigateTo = { step ->
                                    when (step) {
                                        ReviewStudioStep.PRODUCT -> true
                                        ReviewStudioStep.SCRIPT -> product.title.isNotBlank()
                                        ReviewStudioStep.AVATAR -> product.title.isNotBlank() && script.isNotBlank()
                                        ReviewStudioStep.RENDERING, ReviewStudioStep.RESULT -> renderJob.status == "processing" || renderJob.status == "completed"
                                    }
                                }
                            )
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .background(DarkBackground)
                    ) {
                        when (currentStep) {
                            ReviewStudioStep.PRODUCT -> {
                                ProductStepView(
                                    product = product,
                                    isExtracting = isExtracting,
                                    extractError = extractError,
                                    extractSuccess = extractSuccess,
                                    onProductChange = { viewModel.updateProduct(it) },
                                    onExtractUrl = { viewModel.extractProductFromUrl(it) },
                                    onSelectSample = { viewModel.loadSampleProduct(it) },
                                    onNext = { viewModel.setStep(ReviewStudioStep.SCRIPT) }
                                )
                            }
                            ReviewStudioStep.SCRIPT -> {
                                ScriptStepView(
                                    product = product,
                                    script = script,
                                    selectedTone = selectedTone,
                                    selectedDuration = selectedDuration,
                                    isGenerating = isGeneratingScript,
                                    onScriptChange = { viewModel.updateScript(it) },
                                    onToneChange = { viewModel.setTone(it) },
                                    onDurationChange = { viewModel.setDuration(it) },
                                    onRegenerate = { viewModel.generateScript() },
                                    onBack = { viewModel.setStep(ReviewStudioStep.PRODUCT) },
                                    onNext = { viewModel.setStep(ReviewStudioStep.AVATAR) }
                                )
                            }
                            ReviewStudioStep.AVATAR -> {
                                AvatarStepView(
                                    avatars = viewModel.avatarList,
                                    selectedAvatar = selectedAvatar,
                                    aspectRatio = aspectRatio,
                                    product = product,
                                    onAvatarSelect = { viewModel.selectAvatar(it) },
                                    onAspectRatioChange = { viewModel.setAspectRatio(it) },
                                    onBack = { viewModel.setStep(ReviewStudioStep.SCRIPT) },
                                    onStartRender = { viewModel.startRendering() }
                                )
                            }
                            ReviewStudioStep.RENDERING -> {
                                RenderingStepView(
                                    job = renderJob,
                                    product = product,
                                    avatar = selectedAvatar
                                )
                            }
                            ReviewStudioStep.RESULT -> {
                                ResultStepView(
                                    product = product,
                                    avatar = selectedAvatar,
                                    script = script,
                                    aspectRatio = aspectRatio,
                                    onReset = { viewModel.resetAll() }
                                )
                            }
                        }
                    }

                    ApiSettingsDialog(
                        isOpen = isApiDialogOpen,
                        onClose = { isApiDialogOpen = false }
                    )
                }
            }
        }
    }
}
