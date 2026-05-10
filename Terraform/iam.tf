resource "aws_iam_role" "pod_s3_role" {
  name = "${var.project_name}-pod-s3-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = [
          "sts:AssumeRole",
          "sts:TagSession" 
        ]
        Effect = "Allow"
        Principal = {
          Service = "pods.eks.amazonaws.com"
        }
      }
    ]
  })
}

resource "aws_iam_role_policy_attachment" "s3_full_access" {
  policy_arn = "arn:aws:iam::aws:policy/AmazonS3FullAccess"
  role       = aws_iam_role.pod_s3_role.name
}

resource "kubernetes_service_account" "s3_sa" {
  metadata {
    name      = "s3-sa"
    namespace = "default"
  }
  
  depends_on = [module.eks]
}

resource "aws_eks_pod_identity_association" "app_s3_association" {
  cluster_name    = module.eks.cluster_name
  namespace       = kubernetes_service_account.s3_sa.metadata[0].namespace
  service_account = kubernetes_service_account.s3_sa.metadata[0].name
  role_arn        = aws_iam_role.pod_s3_role.arn
  depends_on = [
    aws_iam_role_policy_attachment.s3_full_access,
    kubernetes_service_account.s3_sa
  ]
}