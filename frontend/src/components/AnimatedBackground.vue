<template>
  <div class="animated-bg" ref="bgContainer"></div>
</template>

<script setup>
import { onMounted, onUnmounted, ref } from 'vue'
import * as THREE from 'three'

const bgContainer = ref(null)
let animationId = null
let scene, camera, renderer, mesh

onMounted(() => {
  initThree()
  animate()
})

onUnmounted(() => {
  if (animationId) cancelAnimationFrame(animationId)
  if (renderer) renderer.dispose()
})

function initThree() {
  const container = bgContainer.value
  if (!container) return

  // Scene setup
  scene = new THREE.Scene()

  // Camera
  camera = new THREE.PerspectiveCamera(75, container.clientWidth / container.clientHeight, 0.1, 1000)
  camera.position.z = 30

  // Renderer
  renderer = new THREE.WebGLRenderer({ alpha: true, antialias: true })
  renderer.setSize(container.clientWidth, container.clientHeight)
  renderer.setPixelRatio(Math.min(window.devicePixelRatio, 2))
  container.appendChild(renderer.domElement)

  // Create animated wireframe torus knot (health-themed - flowing, organic)
  const geometry = new THREE.TorusKnotGeometry(12, 3, 128, 16)
  const material = new THREE.MeshBasicMaterial({
    color: 0x10B981,
    wireframe: true,
    transparent: true,
    opacity: 0.15
  })
  mesh = new THREE.Mesh(geometry, material)
  mesh.position.set(60, 0, -20)
  scene.add(mesh)

  // Add floating particles
  const particlesGeometry = new THREE.BufferGeometry()
  const particlesCount = 200
  const posArray = new Float32Array(particlesCount * 3)
  for (let i = 0; i < particlesCount * 3; i++) {
    posArray[i] = (Math.random() - 0.5) * 100
  }
  particlesGeometry.setAttribute('position', new THREE.BufferAttribute(posArray, 3))
  const particlesMaterial = new THREE.PointsMaterial({
    size: 0.3,
    color: 0x10B981,
    transparent: true,
    opacity: 0.4
  })
  const particlesMesh = new THREE.Points(particlesGeometry, particlesMaterial)
  scene.add(particlesMesh)

  // Handle resize
  window.addEventListener('resize', onWindowResize)
}

function animate() {
  animationId = requestAnimationFrame(animate)

  if (mesh) {
    mesh.rotation.x += 0.002
    mesh.rotation.y += 0.003
  }

  renderer.render(scene, camera)
}

function onWindowResize() {
  const container = bgContainer.value
  if (!container || !camera || !renderer) return
  camera.aspect = container.clientWidth / container.clientHeight
  camera.updateProjectionMatrix()
  renderer.setSize(container.clientWidth, container.clientHeight)
}
</script>

<style scoped>
.animated-bg {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  overflow: hidden;
  pointer-events: none;
  z-index: 0;
}

.animated-bg :deep(canvas) {
  width: 100% !important;
  height: 100% !important;
}
</style>
