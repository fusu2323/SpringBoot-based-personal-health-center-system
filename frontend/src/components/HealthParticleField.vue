<template>
  <div class="health-particles" ref="particleContainer"></div>
</template>

<script setup>
import { onMounted, onUnmounted, ref } from 'vue'
import * as THREE from 'three'

const particleContainer = ref(null)
let animationId = null
let scene, camera, renderer, particles

onMounted(() => {
  initParticles()
  animate()
  window.addEventListener('mousemove', onMouseMove)
})

onUnmounted(() => {
  if (animationId) cancelAnimationFrame(animationId)
  if (renderer) renderer.dispose()
  window.removeEventListener('mousemove', onMouseMove)
})

let mouseX = 0, mouseY = 0

function onMouseMove(event) {
  mouseX = (event.clientX / window.innerWidth) * 2 - 1
  mouseY = -(event.clientY / window.innerHeight) * 2 + 1
}

function initParticles() {
  const container = particleContainer.value
  if (!container) return

  scene = new THREE.Scene()
  camera = new THREE.PerspectiveCamera(75, container.clientWidth / container.clientHeight, 0.1, 1000)
  camera.position.z = 30

  renderer = new THREE.WebGLRenderer({ alpha: true, antialias: true })
  renderer.setSize(container.clientWidth, container.clientHeight)
  renderer.setPixelRatio(Math.min(window.devicePixelRatio, 2))
  container.appendChild(renderer.domElement)

  // Create floating particles with health colors
  const particlesGeometry = new THREE.BufferGeometry()
  const particlesCount = 300
  const posArray = new Float32Array(particlesCount * 3)
  const colorArray = new Float32Array(particlesCount * 3)

  const colors = [
    new THREE.Color(0x10B981), // teal
    new THREE.Color(0x3B82F6), // blue
    new THREE.Color(0x6EE7B7), // light teal
    new THREE.Color(0x93C5FD), // light blue
  ]

  for (let i = 0; i < particlesCount; i++) {
    const i3 = i * 3
    posArray[i3] = (Math.random() - 0.5) * 80
    posArray[i3 + 1] = (Math.random() - 0.5) * 40
    posArray[i3 + 2] = (Math.random() - 0.5) * 20

    const color = colors[Math.floor(Math.random() * colors.length)]
    colorArray[i3] = color.r
    colorArray[i3 + 1] = color.g
    colorArray[i3 + 2] = color.b
  }

  particlesGeometry.setAttribute('position', new THREE.BufferAttribute(posArray, 3))
  particlesGeometry.setAttribute('color', new THREE.BufferAttribute(colorArray, 3))

  const particlesMaterial = new THREE.PointsMaterial({
    size: 0.5,
    vertexColors: true,
    transparent: true,
    opacity: 0.6,
    blending: THREE.AdditiveBlending
  })

  particles = new THREE.Points(particlesGeometry, particlesMaterial)
  scene.add(particles)

  window.addEventListener('resize', onResize)
}

function animate() {
  animationId = requestAnimationFrame(animate)

  if (particles) {
    particles.rotation.y += 0.0005
    particles.rotation.x += 0.0002

    // Mouse parallax effect
    particles.rotation.y += mouseX * 0.0005
    particles.rotation.x += mouseY * 0.0005

    // Floating motion
    const positions = particles.geometry.attributes.position.array
    for (let i = 0; i < positions.length; i += 3) {
      positions[i + 1] += Math.sin(Date.now() * 0.001 + i) * 0.01
    }
    particles.geometry.attributes.position.needsUpdate = true
  }

  renderer.render(scene, camera)
}

function onResize() {
  const container = particleContainer.value
  if (!container || !camera || !renderer) return
  camera.aspect = container.clientWidth / container.clientHeight
  camera.updateProjectionMatrix()
  renderer.setSize(container.clientWidth, container.clientHeight)
}
</script>

<style scoped>
.health-particles {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  overflow: hidden;
  pointer-events: none;
  z-index: 0;
}

.health-particles :deep(canvas) {
  width: 100% !important;
  height: 100% !important;
}
</style>
